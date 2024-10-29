package com.draiver.core.utility.audit.appender;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventUtils;

/**
 * @deprecated Please use LoggerAuditAppender instead.  
 * @author Jason Fayling
 *
 */
@Deprecated
public class ConsoleAuditAppender extends AuditAppenderBase {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleAuditAppender.class);

	protected static final String DATETIME_FORMAT = "yyyyMMddHHmmss.SSS Z";

	private boolean prettyPrint = false;
	private boolean addTimestamp = true;

	public ConsoleAuditAppender() {
		this(false, false);
	}

	public ConsoleAuditAppender(boolean prettyPrint, boolean addTimestamp) {
		this.prettyPrint = prettyPrint;
		this.addTimestamp = addTimestamp;
	}

	public boolean getAddTimestamp() {
		return this.addTimestamp;
	}

	public boolean getPrettyPrint() {
		return this.prettyPrint;
	}

	public void setAddTimeStamp(boolean value) {
		this.addTimestamp = value;
	}

	public void setPrettyPrint(boolean value) {
		this.prettyPrint = value;
	}

	@Override
	protected void onAppend(AuditEvent auditEvent, String maskedJson) {
		String output = "";

		if (this.addTimestamp) {
			output = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)) + ": ";
		}

		if (this.prettyPrint) {
			output += AuditEventUtils.fromJson(maskedJson).toJson(this.prettyPrint);
		} else {
			output += maskedJson;
		}
		
		logger.debug(output);
	}

}
