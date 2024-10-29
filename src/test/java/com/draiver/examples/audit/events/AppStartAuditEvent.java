package com.draiver.examples.audit.events;

import com.draiver.core.utility.audit.events.GenericAuditEvent;

public class AppStartAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = -3852811663546639502L;

	private static final String EVENT_NAME = "APP_START";
	private static final String EVENT_TYPE = "AppStartAuditEvent";

	public AppStartAuditEvent(String appName) {
		super(EVENT_NAME, EVENT_TYPE);
		setAppName(appName);
	}

}
