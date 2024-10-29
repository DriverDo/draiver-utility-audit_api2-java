package com.draiver.core.utility.audit.events;

import java.time.ZonedDateTime;
import java.util.Map;

public interface AuditEvent extends EventConfig {

	String getEventCategory();

    ZonedDateTime  getEventCreated();

    String getEventId();

    AuditEventLevel getEventLevel();

    String getEventName();

    String getEventSource();

    AuditEventStatus getEventStatus();

    String getEventStatusDescription();

    long getEventTimestamp();

    String getEventType();

    String getEventVersion();

    String getMessage();

    Map<String, String> getParameters();

    void setEventCategory(String value);

    void setEventLevel(AuditEventLevel value);

    void setEventName(String value);

    void setEventSource(String value);

    void setEventStatus(AuditEventStatus value);

    void setEventStatusDescription(String value);

    void setEventType(String value);

    void setEventVersion(String value);

    void setMessage(String value);

    String toJson();

    String toJson(boolean prettyPrint);

	
	

}
