package com.draiver.core.utility.audit.events;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;

import com.draiver.core.domain.Contextable;

public class AuditEventUtils {

	private AuditEventUtils() {
	}

	public static AuditEvent fromJson(String json) {
		return fromJson(null, json);
	}

	public static AuditEvent fromJson(EventConfig eventConfig, String json) {
		return new MapAuditEvent(eventConfig, json);
	}

	public static <T extends EventConfig> T clone(T copyFrom) {
		try {
			return (T) BeanUtils.cloneBean(copyFrom);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
	}

	public static EventConfig safeToEventConfig(Contextable context) {
		try {
			EventConfig output = new EventConfigImpl();
			if (context.getVisitor() != null) {
				output.setConversationId(context.getVisitor().getConversationId());
				output.setExperienceId(context.getVisitor().getExperienceId());
				output.setSessionId(context.getVisitor().getSessionid());
				output.setTransactionId(context.getVisitor().getTransactionId());
				output.setSequenceId(context.getVisitor().getSequenceId());
				output.setRegion(context.getRegionId());
				output.setTenantId(context.getTenantId());
				output.setPartnerId(context.getPartnerId());
			}
			return output;
		} catch (Exception ex) {
			return null;
		}
	}

	public static EventConfig safeToEventConfig(Object source) {
		if (source == null) {
			return null;
		}
		try {
			if (source instanceof EventConfig) {
				return (EventConfig) source;
			}
			if (source instanceof Contextable) {
				return safeToEventConfig((Contextable) source);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static void populateAuditEvent(AuditEvent copyFrom, AuditEvent copyTo, boolean override) {

		try {
			Map<String, String> beanEventConfig = BeanUtils.describe(copyFrom);
			for (String key : beanEventConfig.keySet()) {
				if (key.equalsIgnoreCase("class") || key.equalsIgnoreCase("parameters")) {
					continue;
				}
				setProperty(key, copyFrom, copyTo, override);
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
			// ignore on purpose
		}

	}

	public static void populateAuditEvent(EventConfig copyFrom, AuditEvent copyTo, boolean override) {

		try {
			Map<String, String> beanEventConfig = BeanUtils.describe(copyFrom);
			for (String key : beanEventConfig.keySet()) {
				if (key.equalsIgnoreCase("class") || key.equalsIgnoreCase("parameters")) {
					continue;
				}
				setProperty(key, copyFrom, copyTo, override);
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
			// ignore on purpose
		}

	}

	public static List<StackTraceElement> fetchFilteredStackTrace(List<String> excludeList) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		StackTraceElement[] filteredStackTrace = Arrays.asList(stackTrace).stream().filter(stack -> !containsStartWith(excludeList, stack.getClassName())).toArray(StackTraceElement[]::new);

		return Arrays.asList(filteredStackTrace);
	}

	public static String fetchCurrentEventSource() {
		return fetchCurrentEventSource(0);
	}

	public static String fetchCurrentEventSource(int backWalkIndex) {
		try {

			if (backWalkIndex < 0) {
				backWalkIndex = 0;
			}

			List<StackTraceElement> filteredStackTrace = fetchFilteredStackTrace(getStackTraceElementExcludeList());

			return filteredStackTrace.get(backWalkIndex).toString();

		} catch (Exception e) {
			return "";
		}
	}

	private static void setProperty(String key, Object copyFrom, Object copyTo, boolean override) {
		try {
			Object fromValue = BeanUtils.getProperty(copyFrom, key);
			Object toValue = BeanUtils.getProperty(copyTo, key);
			if (override || toValue == null || (toValue instanceof String && ((String) toValue).isEmpty())) {
				BeanUtils.setProperty(copyTo, key, fromValue);
			}
		} catch (Exception e) {
			// do nothing on purpose
		}
	}

	@SuppressWarnings("squid:S1612")
	private static boolean containsStartWith(List<String> lookAtList, String startsWith) {
		Optional<String> item = lookAtList.stream().filter(x -> startsWith.startsWith(x)).findFirst();		
		return item.isPresent();
	}

	public static List<String> getStackTraceElementExcludeList() {
		List<String> excludeList = new ArrayList<>();
		excludeList.add("java.");
		excludeList.add("sun.");
		excludeList.add("org.springframework.");
		excludeList.add("com.draiver.core.utility.audit.");
		return excludeList;
	}

	public static void safeAddExtraParameter(AuditEvent auditEvent, String key, String value, boolean override) {
		if (value == null) {
			value = "";
		}
		String newKey = "X-" + key;
		if (auditEvent.getParameters().containsKey(newKey)) {
			if (!override) {
				return;
			}

			auditEvent.getParameters().replace(newKey, value);
			return;
		}
		auditEvent.getParameters().put(newKey, value);
	}

	protected String safeGetExtraParameter(AuditEvent auditEvent, String key) {
		String newKey = "X-" + key;
		return auditEvent.getParameters().getOrDefault(newKey, "");
	}

}
