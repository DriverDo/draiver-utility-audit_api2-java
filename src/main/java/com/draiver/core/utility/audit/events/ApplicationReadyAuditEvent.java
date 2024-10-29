package com.draiver.core.utility.audit.events;

import java.time.ZonedDateTime;

public class ApplicationReadyAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = -5833357828513903084L;
	private static final String EVENTNAME = "ApplicationReady";
	private static final String EVENT_TYPE = "ApplicationReadyAuditEvent";
	private static final String EVENTCATEOGRY = "BOOTSTRAP";

	public ApplicationReadyAuditEvent(String applicationName, EventConfig eventConfig) {
		super(EVENTNAME, EVENT_TYPE, eventConfig);
		this.setEventCategory(EVENTCATEOGRY);
		this.setAppName(applicationName);
		this.setMessage(String.format("%s is ready at %s", applicationName, ZonedDateTime.now().toString()));
	}

	public ApplicationReadyAuditEvent(String applicationName) {
		this(applicationName, null);		
	}

}
