package com.draiver.core.utility.audit.filter;

import com.draiver.core.utility.audit.events.AuditEvent;

public interface AuditEventFilter {

	boolean isAllow();
	boolean isMatch(AuditEvent auditEvent);

}
