package com.draiver.core.utility.audit.appender;

import com.draiver.core.utility.audit.events.AuditEvent;

public class NoOpAuditAppender extends AuditAppenderBase {

	public NoOpAuditAppender() {
		// leave empty on purpose
	}

	@Override
	protected void onAppend(AuditEvent auditEvent, String maskedJson) {
		// do nothing on purpose
	}

}
