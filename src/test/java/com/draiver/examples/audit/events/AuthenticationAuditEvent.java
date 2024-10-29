package com.draiver.examples.audit.events;

import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.GenericAuditEvent;

public class AuthenticationAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = 4429281035812699939L;

	private static final String EVENT_NAME = "AUTHENTICATION";
	private static final String EVENT_TYPE = "AuthenticationAuditEvent";

	private static final String KEY_USERNAME = "userName";

	public AuthenticationAuditEvent(String userName, AuditEventStatus eventStatus) {
		super(EVENT_NAME, EVENT_TYPE);

		setEventStatus(eventStatus);
		safeAddExtraParameter(KEY_USERNAME, userName);
	}

	public String getUserName() {
		return safeGetExtraParameter(KEY_USERNAME);
	}

	public void setUserName(String value) {
		safeAddExtraParameter(KEY_USERNAME, value);
	}

}
