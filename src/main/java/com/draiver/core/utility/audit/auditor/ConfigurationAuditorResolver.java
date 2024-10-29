package com.draiver.core.utility.audit.auditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.draiver.core.utility.audit.appender.NoOpAuditAppender;
import com.draiver.core.utility.audit.configuration.AuditorConfigurationManager;
import com.draiver.core.utility.audit.configuration.XmlAuditorConfigurationManager;

public class ConfigurationAuditorResolver extends AuditorResolverBase {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationAuditorResolver.class);

	private AuditorConfigurationManager configurationManger;

	public ConfigurationAuditorResolver() {
		this(new XmlAuditorConfigurationManager());
	}

	public ConfigurationAuditorResolver(AuditorConfigurationManager configurationManger) {
		super();
		this.configurationManger = configurationManger;
	}

	@Override
	public Auditor createAuditor(Class<?> declaringType) {
		Auditor output = new StandardAuditor();
		output.getAppenders().add(new NoOpAuditAppender());

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Using auditor of type %s", output.getClass().getSimpleName()));
		}

		if (this.configurationManger == null) {
			logger.info("configurationManager == null. Defaulting to NoOpAuditAppender only");
			return output;
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Using configuration manager %s", this.configurationManger.getClass().getSimpleName()));
		}
		output = this.configurationManger.createAuditor(declaringType);
		output.configure(this.configurationManger);

		if (logger.isDebugEnabled()) {
			StringBuilder appenderBuilder = new StringBuilder();
			output.getAppenders().stream().forEach(x -> {
				if (appenderBuilder.length() > 0) {
					appenderBuilder.append(",");
				}
				appenderBuilder.append(x.getClass().getSimpleName());
			});
			logger.debug(String.format("Using appenders: [%s]", appenderBuilder));
		}

		return output;
	}

	public AuditorConfigurationManager getConfigurationManger() {
		return configurationManger;
	}

	public void setConfigurationManger(AuditorConfigurationManager value) {
		this.configurationManger = value;
	}

}
