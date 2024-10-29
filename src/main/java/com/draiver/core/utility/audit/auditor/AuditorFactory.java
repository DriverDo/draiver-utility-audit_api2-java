package com.draiver.core.utility.audit.auditor;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.draiver.core.utility.audit.configuration.XmlAuditorConfigurationManager;

public class AuditorFactory {

	private static final Logger logger = LoggerFactory.getLogger(AuditorFactory.class);

	private AuditorFactory() {
		// default constructor
	}

	public static Auditor createAuditor() {
		return createAuditor((Class<?>) null, (AuditorResolver) null);
	}

	public static Auditor createAuditor(String configFilePath) {
		return createAuditor(null, configFilePath);
	}

	public static Auditor createAuditor(File configFile) {
		return createAuditor(null, configFile.toString());
	}

	public static Auditor createAuditor(Class<?> declaringType) {
		return createAuditor(declaringType, (AuditorResolver) null);
	}

	public static Auditor createAuditor(Class<?> declaringType, String configFilePath) {
		return createAuditor(declaringType, new ConfigurationAuditorResolver(new XmlAuditorConfigurationManager(configFilePath)));
	}

	public static Auditor createAuditor(Class<?> declaringType, File configFile) {
		return createAuditor(declaringType, configFile.toString());
	}

	public static Auditor createAuditor(Class<?> declaringType, AuditorResolver resolver) {
		if (resolver == null) {
			resolver = new ConfigurationAuditorResolver();
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Using audit resolver %s", resolver.getClass().getSimpleName()));
		}
		return resolver.createAuditor(declaringType);
	}

}
