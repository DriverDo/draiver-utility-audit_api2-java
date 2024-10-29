package com.draiver.core.utility.audit.appender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventUtils;

public class LoggerAuditAppender extends AuditAppenderBase {

	private static final Logger logger = LoggerFactory.getLogger(LoggerAuditAppender.class);

	private boolean prettyPrint = false;

	public LoggerAuditAppender() {
		this(false);
	}

	public LoggerAuditAppender(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	public boolean getPrettyPrint() {
		return this.prettyPrint;
	}

	public void setPrettyPrint(boolean value) {
		this.prettyPrint = value;
	}

	@Override
	protected void onAppend(AuditEvent auditEvent, String maskedJson) {
		String output = "";

		if (this.prettyPrint) {
			try {
				output += AuditEventUtils.fromJson(maskedJson).toJson(this.prettyPrint);
			} catch (Exception ex) {
				logger.error("Unable to append audit event", ex);
			}
		} else {
			output += maskedJson;
		}

		logger.debug(output);
	}

}
