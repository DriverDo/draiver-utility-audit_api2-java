package com.draiver.examples.audit.events;

import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.GenericAuditEvent;

public class ServiceInvokeAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = -604844363342529525L;

	private static final String EVENT_NAME = "INVOKE_SERVICE";
	private static final String EVENT_TYPE = "ServiceInvokeAuditEvent";

	private static final String KEY_SERVICE_NAME = "serviceName";

	public ServiceInvokeAuditEvent(String serviceName) {
		this(serviceName, AuditEventStatus.SUCCESS);
	}

	public ServiceInvokeAuditEvent(String serviceName, AuditEventStatus eventStatus) {
		super(EVENT_NAME, EVENT_TYPE);
		setEventStatus(eventStatus);
		safeAddExtraParameter(KEY_SERVICE_NAME, serviceName);
	}

	public String getServiceName() {
		return safeGetExtraParameter(KEY_SERVICE_NAME);
	}

	public void setServiceName(String value) {
		safeAddExtraParameter(KEY_SERVICE_NAME, value);
	}

}
