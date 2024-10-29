package com.draiver.core.utility.audit.events;

public class GenericAuditEvent extends AuditEventBase {

	private static final long serialVersionUID = 8700431001955611382L;
	private static final String TYPE_NAME = "GenericAuditEvent";

	public GenericAuditEvent(String eventName) {
		this(eventName, TYPE_NAME, AuditEventLevel.INFO, AuditEventStatus.SUCCESS);
	}

	public GenericAuditEvent(String eventName, EventConfig eventConfig) {
		this(eventName, TYPE_NAME, eventConfig, AuditEventLevel.INFO, AuditEventStatus.SUCCESS);
	}

	public GenericAuditEvent(String eventName, String eventType) {
		this(eventName, eventType, AuditEventLevel.INFO, AuditEventStatus.SUCCESS);
	}

	public GenericAuditEvent(String eventName, String eventType, EventConfig eventConfig) {
		this(eventName, eventType, eventConfig, AuditEventLevel.INFO, AuditEventStatus.SUCCESS);
	}

	public GenericAuditEvent(String eventName, AuditEventLevel eventLevel, AuditEventStatus eventStatus) {
		this(eventName, TYPE_NAME, eventLevel, eventStatus);
	}

	public GenericAuditEvent(String eventName, EventConfig eventConfig, AuditEventLevel eventLevel, AuditEventStatus eventStatus) {
		this(eventName, TYPE_NAME, eventConfig, eventLevel, eventStatus);
	}

	public GenericAuditEvent(String eventName, String eventType, AuditEventLevel eventLevel, AuditEventStatus eventStatus) {
		this(eventName, eventType, null, eventLevel, eventStatus);
	}

	public GenericAuditEvent(String eventName, String eventType, EventConfig eventConfig, AuditEventLevel eventLevel, AuditEventStatus eventStatus) {
		super(eventConfig, null);
		setEventName(eventName);
		setEventLevel(eventLevel);
		setEventStatus(eventStatus);
		setEventVersion("1.0");
		setEventType(eventType);
		setEventCategory("UNKNOWN");
	}

}
