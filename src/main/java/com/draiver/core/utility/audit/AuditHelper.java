package com.draiver.core.utility.audit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.draiver.core.domain.SimpleContext;
import com.draiver.core.utility.BeanUtility;
import com.draiver.core.utility.audit.auditor.Auditor;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.ExceptionAuditEvent;
import com.draiver.core.utility.audit.events.ServiceRequestAuditEvent;
import com.draiver.core.utility.audit.events.UsageAuditEvent;
import com.draiver.core.utility.context.DraiverThreadContext;

public class AuditHelper {

	private static final Logger logger = LoggerFactory.getLogger(AuditHelper.class);

	public static final String AUDIT_CATEGORY = "GENERAL";

	public static void asyncSendAuditEvent(Auditor auditor, AuditEvent auditEvent) {
		if (auditEvent == null) {
			return;
		}

		configureAuditEvent(auditEvent);
		auditor.setInsertEventSource(false);
		auditor.auditAsync(auditEvent);
	}

	public static void asyncSendAuditEvent(Auditor auditor, Collection<AuditEvent> auditEvents) {
		if (auditEvents == null) {
			return;
		}

		configureAuditEvents(auditEvents);
		auditor.setInsertEventSource(false);
		auditor.auditAsync(auditEvents);
	}

	public static void asyncSendAuditEvent(Auditor auditor, AuditEvent[] auditEvents) {
		if (auditEvents == null) {
			return;
		}

		configureAuditEvents(auditEvents);
		auditor.setInsertEventSource(false);
		auditor.auditAsync(auditEvents);
	}

	public static void asyncSendExceptionAuditEvent(Auditor auditor, Exception ex) {
		asyncSendAuditEvent(auditor, new ExceptionAuditEvent(ex));
	}

	public static void asyncSendServiceRequestAuditEvent(Auditor auditor, Class<?> clazz, String methodName, String message, AuditEventStatus eventStatus) {
		asyncSendServiceRequestAuditEvent(auditor, clazz, methodName, message, eventStatus, null);
	}

	public static void asyncSendServiceRequestAuditEvent(Auditor auditor, Class<?> clazz, String methodName, String message, AuditEventStatus eventStatus, Map<String, Object> payloadData) {
		asyncSendAuditEvent(auditor, createServiceRequestAuditEvent(clazz, methodName, message, eventStatus, payloadData));
	}

	public static void asyncSendUsageAuditEvent(Auditor auditor, Class<?> clazz, String methodName) {
		asyncSendUsageAuditEvent(auditor, clazz, methodName, AuditEventStatus.SUCCESS);
	}

	public static void asyncSendUsageAuditEvent(Auditor auditor, Class<?> clazz, String methodName, AuditEventStatus eventStatus) {
		asyncSendUsageAuditEvent(auditor, clazz, methodName, eventStatus, null);
	}

	public static void asyncSendUsageAuditEvent(Auditor auditor, Class<?> clazz, String methodName, AuditEventStatus eventStatus, Map<String, Object> payloadData) {
		UsageAuditEvent auditEvent = createUsageAuditEvent(clazz, methodName, payloadData);
		auditEvent.setEventStatus(eventStatus);
		asyncSendAuditEvent(auditor, auditEvent);
	}

	public static void asyncSendUsageAuditEvent(Auditor auditor, Class<?> clazz, String methodName, Map<String, Object> payloadData) {
		asyncSendUsageAuditEvent(auditor, clazz, methodName, AuditEventStatus.SUCCESS, payloadData);
	}

	public static void configureAuditEvents(AuditEvent[] auditEvents) {
		if (auditEvents == null) {
			return;
		}

		configureAuditEvents(Arrays.asList(auditEvents));
	}

	public static void configureAuditEvents(Collection<AuditEvent> auditEvents) {
		if (auditEvents == null) {
			return;
		}

		auditEvents.stream().forEach(AuditHelper::configureAuditEvent);
	}

	public static void configureAuditEvent(AuditEvent auditEvent) {
		if (auditEvent == null) {
			return;
		}

		if ( contextIsSet( DraiverThreadContext.KEY_SEQUENCE_ID ) ) {
			auditEvent.setSequenceId(getStringFromContext(DraiverThreadContext.KEY_SEQUENCE_ID));
		}

		if ( contextIsSet( DraiverThreadContext.KEY_TRANSACTION_ID ) ) {
			auditEvent.setTransactionId(getStringFromContext(DraiverThreadContext.KEY_TRANSACTION_ID));
		}

		if ( contextIsSet( DraiverThreadContext.KEY_SESSION_ID ) ) {
			auditEvent.setSessionId(getStringFromContext(DraiverThreadContext.KEY_SESSION_ID));
		}

		if ( contextIsSet( DraiverThreadContext.KEY_CONVERSATION_ID ) ) {
			auditEvent.setConversationId(getStringFromContext(DraiverThreadContext.KEY_CONVERSATION_ID));
		}

		if ( contextIsSet( DraiverThreadContext.KEY_EXPERIENCE_ID ) ) {
			auditEvent.setExperienceId(getStringFromContext(DraiverThreadContext.KEY_EXPERIENCE_ID));
		}

		if ( contextIsSet( DraiverThreadContext.KEY_TENANT_ID ) ) {
			auditEvent.setTenantId(getStringFromContext(DraiverThreadContext.KEY_TENANT_ID));
		}

		if ( contextIsSet( DraiverThreadContext.KEY_PARTNER_ID ) ) {
			auditEvent.setPartnerId(getStringFromContext(DraiverThreadContext.KEY_PARTNER_ID));
		}

		try {
			List<String> excludeList = new ArrayList<>();
			excludeList.add("java.");
			excludeList.add("com.draiver.core.utility.audit.events.AuditEventUtils");
			excludeList.add("com.draiver.core.utility.audit.AuditHelper");

			List<StackTraceElement> stackTrace = AuditEventUtils.fetchFilteredStackTrace(excludeList);
			auditEvent.setEventSource(stackTrace.get(0).toString());
		} catch (Exception ex) {
			logger.error("Unable to configure audit event", ex);
		}
	}

	private static String getStringFromContext(String key) {
		Object value = DraiverThreadContext.get( key );
		return value == null ? null : value.toString();
	}

	private static boolean contextIsSet(String key) {
		Object contextValue = DraiverThreadContext.get( key );
		return contextValue != null && StringUtils.isNotEmpty( contextValue.toString() );
	}

	public static ServiceRequestAuditEvent createServiceRequestAuditEvent(Class<?> clazz, String methodName) {
		return createServiceRequestAuditEvent(clazz, methodName, "SUCCESS", AuditEventStatus.SUCCESS, null);
	}

	public static ServiceRequestAuditEvent createServiceRequestAuditEvent(Class<?> clazz, String methodName, String message, AuditEventStatus eventStatus) {
		return createServiceRequestAuditEvent(clazz, methodName, message, eventStatus, null);
	}

	public static ServiceRequestAuditEvent createServiceRequestAuditEvent(Class<?> clazz, String methodName, String message, AuditEventStatus eventStatus, Map<String, Object> payloadData) {
		SimpleContext context = new SimpleContext();
		Map<String, Object> eventPayload = new HashMap<>();
		eventPayload.put("context", BeanUtility.toJSON(context));
		if (payloadData != null) {
			eventPayload.putAll(payloadData);
		}
		ServiceRequestAuditEvent event1 = new ServiceRequestAuditEvent(clazz.getCanonicalName());
		event1.setServicePayload(eventPayload);
		event1.setServiceMethod(methodName);
		event1.setEventStatus(eventStatus);
		event1.setMessage(message);
		return event1;
	}

	public static UsageAuditEvent createUsageAuditEvent(Class<?> clazz, String methodName, Map<String, Object> payloadData) {
		SimpleContext context = new SimpleContext();
		Map<String, Object> eventPayload = new HashMap<>();
		eventPayload.put("context", BeanUtility.toJSON(context));
		if (payloadData != null) {
			eventPayload.putAll(payloadData);
		}
		UsageAuditEvent event1 = new UsageAuditEvent();
		event1.setUsageItemProvider(clazz.getSimpleName());
		event1.setUsageItem(methodName);
		event1.setUsageItemData(BeanUtility.toJSON(eventPayload));
		event1.setUsageAmount(1);
		return event1;
	}

	public static void sendAuditEvent(Auditor auditor, AuditEvent auditEvent) {
		if (auditEvent == null) {
			return;
		}

		configureAuditEvent(auditEvent);
		auditor.auditAsync(auditEvent);
	}

	public static void sendAuditEvent(Auditor auditor, AuditEvent[] auditEvents) {
		if (auditEvents == null) {
			return;
		}

		configureAuditEvents(auditEvents);
		auditor.auditAsync(auditEvents);
	}

	public static void sendAuditEvent(Auditor auditor, Collection<AuditEvent> auditEvents) {
		if (auditEvents == null) {
			return;
		}

		configureAuditEvents(auditEvents);
		auditor.auditAsync(auditEvents);
	}

	public static void sendServiceRequestAuditEvent(Auditor auditor, Class<?> clazz, String methodName, String message, AuditEventStatus eventStatus) {
		sendServiceRequestAuditEvent(auditor, clazz, methodName, message, eventStatus, null);
	}

	public static void sendServiceRequestAuditEvent(Auditor auditor, Class<?> clazz, String methodName, String message, AuditEventStatus eventStatus, Map<String, Object> payloadData) {
		sendAuditEvent(auditor, createServiceRequestAuditEvent(clazz, methodName, message, eventStatus, payloadData));
	}

	public static void sendUsageAuditEvent(Auditor auditor, Class<?> clazz, String methodName, Map<String, Object> payloadData) {
		sendAuditEvent(auditor, createUsageAuditEvent(clazz, methodName, payloadData));
	}

	private AuditHelper() {
	}

}