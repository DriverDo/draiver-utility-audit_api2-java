package com.draiver.core.utility.audit.events;

import java.util.HashMap;
import java.util.Map;

import com.draiver.core.utility.BeanUtility;
import org.apache.commons.lang3.StringUtils;

/**
 * Service Request Audit Event
 * 
 * @author jason.fayling@reconvelocity.com
 *
 */
public class ServiceRequestAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = 8041849721853290452L;

	private static final String TYPE_NAME = "ServiceRequestAuditEvent";
	private static final String EVENTNAME = "ServiceRequest";
	private static final String EVENTCATEGORY = "SERVICE_INFO";

	private static final String PARAM_SERVICENAME = "ServiceName";
	private static final String PARAM_SERVICEVERSION = "ServiceVersion";
	private static final String PARAM_SERVICEPAYLOAD = "ServicePayload";
	private static final String PARAM_SERVICEMETHOD = "ServiceMethod";

	public ServiceRequestAuditEvent(String serviceName) {
		this(serviceName, null);
		this.setEventSource();
	}

	public ServiceRequestAuditEvent(String serviceName, EventConfig eventConfig) {
		super(EVENTNAME, TYPE_NAME, eventConfig, AuditEventLevel.DEBUG, AuditEventStatus.SUCCESS);
		this.setServiceName(serviceName);
		this.setEventCategory(EVENTCATEGORY);
		this.setEventSource();
	}

	public String getServiceMethod() {
		return safeGetExtraParameter(PARAM_SERVICEMETHOD);
	}

	public String getServiceName() {
		return safeGetExtraParameter(PARAM_SERVICENAME);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getServicePayload() {
		String json =  safeGetExtraParameter(PARAM_SERVICEPAYLOAD);
		return (Map<String, Object>) BeanUtility.fromJSON(json, HashMap.class);
	}
	
	public String getServiceVersion() {
		return safeGetExtraParameter(PARAM_SERVICEVERSION);
	}
	
	public void setServiceMethod(String value) {
		safeAddExtraParameter(PARAM_SERVICEMETHOD, value);
	}	
	
	public void setServiceName(String value) {
		safeAddExtraParameter(PARAM_SERVICENAME, value);
	}
	
	public void setServicePayload(Map<String,Object> value) {
		safeAddExtraParameter(PARAM_SERVICEPAYLOAD, BeanUtility.toJSON(value, false));
	}
	
	public void setServiceVersion(String value) {
		safeAddExtraParameter(PARAM_SERVICEVERSION, value);
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();
		if (StringUtils.isNotEmpty(getServiceName()) && (StringUtils.isNotBlank(getServiceMethod()))) {
			return String.format("%s.%s: %s", getServiceName(), getServiceMethod(), message);
		}
		else return message;
	}

	private void setEventSource() {
		setEventSource(AuditEventUtils.fetchCurrentEventSource(0));
	}
}
