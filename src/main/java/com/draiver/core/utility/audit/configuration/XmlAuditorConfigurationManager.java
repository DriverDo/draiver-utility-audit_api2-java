package com.draiver.core.utility.audit.configuration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.draiver.core.utility.audit.appender.AuditAppender;
import com.draiver.core.utility.audit.appender.NoOpAuditAppender;
import com.draiver.core.utility.audit.auditor.Auditor;
import com.draiver.core.utility.audit.auditor.StandardAuditor;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.exception.AuditException;
import com.draiver.core.utility.audit.filter.AuditEventFilter;
import com.draiver.core.utility.audit.masker.AuditEventMasker;

public class XmlAuditorConfigurationManager implements AuditorConfigurationManager {

	private static final Logger logger = LoggerFactory.getLogger(XmlAuditorConfigurationManager.class);

	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ATTRIBUTE_MINLEVEL = "minLevel";
	private static final String ATTRIBUTE_DECLARINGTYPEREGEX = "declaringTypeRegEx";

	private static final String DEFAULT_CONFIG_FILE = "/auditor.xml";

	private static final String REGEX_RESOLVER = "\\[\\[RESOLVER\\]\\]\\:\\{\\s*class\\s*=\\s*'(.*?)'\\s*value\\s*=\\s*'(.*?)'\\s*\\}";
	private static final Pattern _resolverRegexPattern = Pattern.compile(REGEX_RESOLVER, Pattern.MULTILINE);

	private String xml;

	public XmlAuditorConfigurationManager() {
		this(DEFAULT_CONFIG_FILE);
	}

	public XmlAuditorConfigurationManager(InputStream xmlInputStream) {
		xml = inputStreamToString(xmlInputStream);
	}

	public XmlAuditorConfigurationManager(File xmlFile) {
		this(xmlFile.toString());
	}

	public XmlAuditorConfigurationManager(String xmlFilePath) {
		File tmpFile = new File(xmlFilePath);
		if (tmpFile.exists()) {
			try {
				xml = new String(Files.readAllBytes(Paths.get(xmlFilePath)), StandardCharsets.UTF_8);
			} catch (IOException e) {
				logger.error(String.format("Unable to read file %s", xmlFilePath), e);
			}
		} else {
			xml = inputStreamToString(XmlAuditorConfigurationManager.class.getResourceAsStream(xmlFilePath));
		}
	}

	private static AuditAppender createAuditAppender(Node appenderNode) {
		try {

			if (appenderNode == null || !appenderNode.hasAttributes()) {
				return null;
			}

			String classPath = appenderNode.getAttributes().getNamedItem(ATTRIBUTE_CLASS).getTextContent();
			String minLevel = appenderNode.getAttributes().getNamedItem(ATTRIBUTE_MINLEVEL).getTextContent();

			
			AuditAppender auditAppender = (AuditAppender) Class.forName(classPath,true,Thread.currentThread().getContextClassLoader()).getDeclaredConstructor().newInstance();
			auditAppender.setMinLevel(AuditEventLevel.valueOf(minLevel));

			populateParameters(auditAppender, appenderNode);
			populateAppenderFilters(auditAppender, appenderNode);
			populateAppenderMaskers(auditAppender, appenderNode);

			return auditAppender;
		} catch (Exception e) {
			return null;
		}
	}

	private static NodeList executeXPath(String query, Object xmlDoc) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			return (NodeList) xPath.compile(query).evaluate(xmlDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	private static void populateAppenderFilters(AuditAppender auditAppender, Node appenderNode) {
		NodeList filterNodeList = executeXPath("filters/filter", appenderNode);
		if (filterNodeList != null && filterNodeList.getLength() > 0) {
			for (int i = 0; i < filterNodeList.getLength(); i++) {
				try {
					Node filterNode = filterNodeList.item(i);

					String classPath = filterNode.getAttributes().getNamedItem(ATTRIBUTE_CLASS).getTextContent();
					AuditEventFilter filter = (AuditEventFilter) Class.forName(classPath,true,Thread.currentThread().getContextClassLoader()).getDeclaredConstructor().newInstance();

					populateParameters(filter, filterNode);
					auditAppender.getFilters().add(filter);

				} catch (Exception e) {
					// eat this
				}
			}
		}

	}

	private static void populateAppenderMaskers(AuditAppender auditAppender, Node appenderNode) {
		NodeList maskerNodeList = executeXPath("maskers/masker", appenderNode);
		if (maskerNodeList != null && maskerNodeList.getLength() > 0) {
			for (int i = 0; i < maskerNodeList.getLength(); i++) {
				try {
					Node maskerNode = maskerNodeList.item(i);

					String classPath = maskerNode.getAttributes().getNamedItem(ATTRIBUTE_CLASS).getTextContent();
					AuditEventMasker masker = (AuditEventMasker) Class.forName(classPath,true,Thread.currentThread().getContextClassLoader()).getDeclaredConstructor().newInstance();

					populateParameters(masker, maskerNode);
					auditAppender.getMaskers().add(masker);

				} catch (Exception e) {
					// eat this
				}
			}
		}

	}

	private static void populateParameters(Object targetObject, Node appenderNode) {
		NodeList paramNodeList = executeXPath("parameters/param", appenderNode);
		if (paramNodeList != null && paramNodeList.getLength() > 0) {
			for (int i = 0; i < paramNodeList.getLength(); i++) {
				try {
					Node paramNode = paramNodeList.item(i);
					String propertyName = paramNode.getAttributes().getNamedItem("name").getTextContent();
					String propertyValue = paramNode.getAttributes().getNamedItem("value").getTextContent();

					BeanUtils.setProperty(targetObject, propertyName, resolveConfigurationParameter(propertyValue));
				} catch (Exception e) {
					// eat this
				}
			}
		}
	}

	private static String resolveConfigurationParameter(String value) {
		if (value == null || value.isEmpty()) {
			return "";
		}

		try {
			Matcher matcher = _resolverRegexPattern.matcher(value);

			String resolverClassPath = null;
			String resolverValue = null;

			if (!matcher.find()) {
				return value;
			}

			resolverClassPath = matcher.group(1);
			resolverValue = matcher.group(2);

			ConfigurationParameterResolver resolver = (ConfigurationParameterResolver) Class.forName(resolverClassPath,true,Thread.currentThread().getContextClassLoader()).getDeclaredConstructor().newInstance();
			return resolver.resolve(resolverValue);
		} catch (Exception e) {
			// eat this on purpose
			return "";
		}

	}

	@Override
	public void configure(Auditor auditor) {
		try {

			if (logger.isDebugEnabled() && !StringUtils.isEmpty(xml)) {
				logger.debug(String.format("Using config file %n%s", xml));
			}

			if (auditor == null || StringUtils.isEmpty(xml)) {
				logger.info("Unable to configure auditor. Auditor or xmlFile is null");
				return;
			}

			@SuppressWarnings("squid:S2755")
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

			auditor.getAppenders().clear();
			NodeList results = executeXPath("//appender", doc);
			if (results != null && results.getLength() > 0) {
				for (int i = 0; i < results.getLength(); i++) {
					AuditAppender auditAppender = createAuditAppender(results.item(i));
					if (auditAppender == null) {
						continue;
					}
					auditor.getAppenders().add(auditAppender);
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Added appender %s", auditAppender.getClass().getSimpleName()));
					}
				}
			}

		} catch (Exception e) {
			throw new AuditException(e);
		}
	}

	@Override
	public Auditor createAuditor(Class<?> declaringType) {
		Auditor defaultAuditor = new StandardAuditor();
		defaultAuditor.getAppenders().add(new NoOpAuditAppender());

		try {

			if (declaringType == null) {
				return defaultAuditor;
			}

			@SuppressWarnings("squid:S2755")
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

			NodeList auditorNodes = executeXPath("//auditor", doc);
			if (auditorNodes == null) {
				return defaultAuditor;
			}

			for (int i = 0; i < auditorNodes.getLength(); i++) {
				Node auditorNode = auditorNodes.item(i);
				Auditor auditor = createAuditorFromXmlNode(auditorNode, declaringType);
				if (auditor != null) {
					return auditor;
				}
			}

			// fall back
			return defaultAuditor;

		} catch (Exception e) {
			return defaultAuditor;
		}
	}

	private Auditor createAuditorFromXmlNode(Node auditorNode, Class<?> declaringType) {
		try {
			String declaringTypeRegEx = auditorNode.getAttributes().getNamedItem(ATTRIBUTE_DECLARINGTYPEREGEX).getTextContent();
			String classPath = auditorNode.getAttributes().getNamedItem(ATTRIBUTE_CLASS).getTextContent();

			if (declaringTypeRegEx == null || declaringTypeRegEx.isEmpty() || classPath == null || classPath.isEmpty()) {
				return null;
			}

			Pattern pattern = Pattern.compile(declaringTypeRegEx, Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(declaringType.getName());
			if (matcher.find()) {
				return (Auditor) Class.forName(classPath,true,Thread.currentThread().getContextClassLoader()).getDeclaredConstructor().newInstance();
			}

		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private String inputStreamToString(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			return br.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			logger.error("Unable to access xml input stream", e);
		}

		return null;
	}

}
