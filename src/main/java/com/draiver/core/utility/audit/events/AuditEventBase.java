package com.draiver.core.utility.audit.events;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AuditEventBase implements AuditEvent, EventConfig, Serializable {

	private static final Logger logger = LoggerFactory.getLogger(AuditEventBase.class);

	private static final long serialVersionUID = 6963926091825491327L;

	protected static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	protected static final String UNKNOWN = "Unknown";
	protected static final String EMPTY = "";

	protected static final String APP_NAME = "AppName";
	protected static final String MODULE_NAME = "ModuleName";
	protected static final String ENV = "Env";
	protected static final String DIVISION = "Division";
	protected static final String MACHINE = "Machine";
	protected static final String NAMESPACE = "Namespace";

	protected static final String REGION = "Region";
	protected static final String TENANT_ID = "TenantId";
	protected static final String PARTNER_ID = "PartnerId";

	protected static final String CONVERSATION_ID = "ConversationId";
	protected static final String SESSION_ID = "SessionId";
	protected static final String EXPERIENCE_ID = "ExperienceId";
	protected static final String TRANSACTION_ID = "TransactionId";
	protected static final String SEQUENCE_ID = "SequenceId";

	protected static final String EVENT_ID = "EventId";
	protected static final String EVENT_NAME = "EventName";
	protected static final String EVENT_SOURCE = "EventSource";
	protected static final String EVENT_CATEGORY = "EventCategory";
	protected static final String EVENT_LEVEL = "EventLevel";
	protected static final String EVENT_CREATED = "EventCreated";
	protected static final String EVENT_TIMESTAMP = "EventTimestamp";
	protected static final String EVENT_STATUS = "EventStatus";
	protected static final String EVENT_VERSION = "EventVersion";
	protected static final String EVENT_TYPE = "EventType";
	protected static final String EVENT_STATUS_DESCRIPTION = "EventStatusDescription";

	protected static final String MESSAGE = "Message";

	private static final List<String> _internalKeyList = new ArrayList<>();

	// Initialize key list map
	static {
		_internalKeyList.add(EVENT_ID);
		_internalKeyList.add(EVENT_CREATED);
		_internalKeyList.add(EVENT_TIMESTAMP);
		_internalKeyList.add(EVENT_LEVEL);
		_internalKeyList.add(EVENT_TYPE);
		_internalKeyList.add(EVENT_SOURCE);
		_internalKeyList.add(EVENT_NAME);
		_internalKeyList.add(EVENT_STATUS);
		_internalKeyList.add(EVENT_CATEGORY);
		_internalKeyList.add(EVENT_VERSION);
		_internalKeyList.add(EVENT_STATUS_DESCRIPTION);

		_internalKeyList.add(ENV);
		_internalKeyList.add(DIVISION);
		_internalKeyList.add(MACHINE);
		_internalKeyList.add(MODULE_NAME);
		_internalKeyList.add(APP_NAME);
		_internalKeyList.add(NAMESPACE);

		_internalKeyList.add(REGION);
		_internalKeyList.add(TENANT_ID);
		_internalKeyList.add(PARTNER_ID);

		_internalKeyList.add(EXPERIENCE_ID);
		_internalKeyList.add(CONVERSATION_ID);
		_internalKeyList.add(SESSION_ID);
		_internalKeyList.add(TRANSACTION_ID);
		_internalKeyList.add(SEQUENCE_ID);

		_internalKeyList.add(MESSAGE);
	}

	@JsonPropertyOrder(alphabetic = true)
	private Map<String, String> parameters = new TreeMap<>();

	protected AuditEventBase(EventConfig eventConfig, List<String> keyList) {
		this(keyList);

		if (eventConfig == null) {
			return;
		}

		try {
			Map<String, String> beanEventConfig = BeanUtils.describe(eventConfig);
			beanEventConfig.keySet().forEach(key -> {
				if (key.equals("class")) {
					return;
				}
				try {
					BeanUtils.setProperty(this, key, beanEventConfig.get(key));
				} catch (IllegalAccessException | InvocationTargetException e) {
					// ignore on purpose
				}
			});
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
			// ignore on purpose
		}

	}

	/**
	 * Constructor
	 * 
	 * @param keyList
	 */
	protected AuditEventBase(List<String> keyList) {

		synchronized (this) {

			// add all the supplied keys to the parameter map
			if (parameters == null) {
				parameters = new TreeMap<>();
			}

			if (keyList != null) {
				try {
					for (String x : Collections.unmodifiableList(new ArrayList<>(keyList))) {
						parameters.put(x, "");
					}
				} catch (Exception ex) {
					logger.error("Unable to initialize parametres. Allowing process to continue.", ex);
				}
			}

			List<String> tmpKeyList = Collections.unmodifiableList(new ArrayList<>(_internalKeyList));
			for (String x : tmpKeyList) {
				try {
					if (parameters == null) {
						parameters = new TreeMap<>();
					}
					if (!parameters.containsKey(x)) {
						parameters.put(x, "");
					}
				} catch (Exception ex) {
					logger.error("Unable to initialize parametres. Allowing process to continue.");
				}
			}

			ZonedDateTime createdDate = ZonedDateTime.now();

			safeAddParameter(EVENT_ID, UUID.randomUUID().toString());
			safeAddParameter(EVENT_VERSION, "1.0");
			safeAddParameter(EVENT_CREATED, createdDate.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
			safeAddTypedParameter(EVENT_TIMESTAMP, createdDate.toEpochSecond());

			safeAddParameter(EVENT_LEVEL, AuditEventLevel.INFO.toString());
			safeAddParameter(EVENT_STATUS, AuditEventStatus.SUCCESS.toString());
			try {
				safeAddParameter(MACHINE, java.net.InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				safeAddParameter(MACHINE, UNKNOWN);
			}
		}

	}

	@Override
	public String getAppName() {
		return parameters.getOrDefault(APP_NAME, EMPTY);
	}

	@Override
	public String getConversationId() {
		return parameters.getOrDefault(CONVERSATION_ID, EMPTY);
	}

	@Override
	public String getDivision() {
		return parameters.getOrDefault(DIVISION, EMPTY);
	}

	@Override
	public String getEnv() {
		return parameters.getOrDefault(ENV, EMPTY);
	}

	@Override
	public String getEventCategory() {
		return parameters.getOrDefault(EVENT_CATEGORY, EMPTY);
	}

	@Override
	public ZonedDateTime getEventCreated() {
		return ZonedDateTime.parse(parameters.getOrDefault(EVENT_CREATED, EMPTY), DateTimeFormatter.ofPattern(DATETIME_FORMAT));
	}

	@Override
	public String getEventId() {
		return parameters.getOrDefault(EVENT_ID, EMPTY);
	}

	@Override
	public AuditEventLevel getEventLevel() {
		return AuditEventLevel.valueOf(parameters.getOrDefault(EVENT_LEVEL, EMPTY));
	}

	@Override
	public String getEventName() {
		return parameters.getOrDefault(EVENT_NAME, EMPTY);
	}

	@Override
	public String getEventSource() {
		return parameters.getOrDefault(EVENT_SOURCE, EMPTY);
	}

	@Override
	public AuditEventStatus getEventStatus() {
		return AuditEventStatus.valueOf(parameters.getOrDefault(EVENT_STATUS, EMPTY));
	}

	@Override
	public String getEventStatusDescription() {
		return parameters.getOrDefault(EVENT_STATUS_DESCRIPTION, EMPTY);
	}

	@Override
	public long getEventTimestamp() {		
		return safeGetTypedParameter(EVENT_TIMESTAMP,(long)0);
	}

	@Override
	public String getEventType() {
		return parameters.getOrDefault(EVENT_TYPE, EMPTY);
	}

	@Override
	public String getEventVersion() {
		return parameters.getOrDefault(EVENT_VERSION, EMPTY);
	}

	@Override
	public String getExperienceId() {
		return parameters.getOrDefault(EXPERIENCE_ID, EMPTY);
	}

	@Override
	public String getMachine() {
		return parameters.getOrDefault(MACHINE, EMPTY);
	}

	@Override
	public String getMessage() {
		return parameters.getOrDefault(MESSAGE, EMPTY);
	}

	@Override
	public String getModuleName() {
		return parameters.getOrDefault(MODULE_NAME, EMPTY);
	}

	@Override
	public String getNamespace() {
		return parameters.getOrDefault(NAMESPACE, EMPTY);
	}

	@Override
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	@Override
	public String getPartnerId() {
		return parameters.getOrDefault(PARTNER_ID, EMPTY);
	}

	@Override
	public String getRegion() {
		return parameters.getOrDefault(REGION, EMPTY);
	}

	@Override
	public String getSequenceId() {
		return parameters.getOrDefault(SEQUENCE_ID, EMPTY);
	}

	@Override
	public String getSessionId() {
		return parameters.getOrDefault(SESSION_ID, EMPTY);
	}

	private <T> String getStringValue(T value) {
		if (value instanceof Integer) {
			return specialFunctionNumber((Integer) value);
		} else if (value instanceof Double) {
			return specialFunctionNumber((Double) value);
		} else if (value instanceof Float) {
			return specialFunctionNumber((Float) value);
		} else if (value instanceof Long) {
			return specialFunctionNumber((Long) value);
		} else if (value instanceof Boolean) {
			return specialFunctionBoolean((Boolean) value);
		}
		return value.toString();
	}

	@Override
	public String getTenantId() {
		return parameters.getOrDefault(TENANT_ID, EMPTY);
	}

	@Override
	public String getTransactionId() {
		return parameters.getOrDefault(TRANSACTION_ID, EMPTY);
	}

	private <T> T getTypedValue(String value, Class<T> clazz) {
		if (clazz.isAssignableFrom(Integer.class)) {
			return (T) Integer.valueOf(value);
		} else if (clazz.isAssignableFrom(Double.class)) {
			return (T) Double.valueOf(value);
		} else if (clazz.isAssignableFrom(Long.class)) {
			return (T) Long.valueOf(value);
		} else if (clazz.isAssignableFrom(Float.class)) {
			return (T) Float.valueOf(value);
		} else if (clazz.isAssignableFrom(Boolean.class)) {
			return (T) Boolean.valueOf(value);
		}
		return (T) value.toString();
	}

	protected void safeAddExtraParameter(String key, String value) {
		safeAddExtraTypedParameter(key, value);
	}

	protected <T> void safeAddExtraTypedParameter(String key, T value) {
		String newValue = "";
		if (value != null) {			
			newValue = getStringValue(value);
		}
		String newKey = "X-" + key;
		if (parameters.containsKey(newKey)) {
			parameters.replace(newKey, newValue);
			return;
		}
		parameters.put(newKey, newValue);
	}

	private void safeAddParameter(String key, String value) {		
		safeAddTypedParameter(key, value);
	}

	private void safeAddReplaceParameter(String key, String value) {		
		safeAddReplaceTypedParameter(key, value);
	}

	private <T> void safeAddReplaceTypedParameter(String key, T value) {
		String newValue = "";
		if (value != null) {			
			newValue = getStringValue(value);
		}
		if (parameters.containsKey(key)) {
			parameters.replace(key, newValue);
			return;
		}
		parameters.put(key, newValue);
	}

	private <T> void safeAddTypedParameter(String key, T value) {
		String newValue = "";
		if (value != null) {			
			newValue = getStringValue(value);
		}
		if (parameters.containsKey(key)) {
			parameters.put(key, newValue);
		}
	}

	protected String safeGetExtraParameter(String key) {
		return safeGetExtraTypedParameter(key, "");
	}

	protected <T> T safeGetExtraTypedParameter(String key, T defaultValue) {
		String newKey = "X-" + key;
		String value = transformSpecialFunctionsParameter(parameters.getOrDefault(newKey, defaultValue.toString()));
		return (T) getTypedValue(value, defaultValue.getClass());
	}
	
	private <T> T safeGetTypedParameter(String key, T defaultValue) {
		String value = transformSpecialFunctionsParameter(parameters.getOrDefault(key, defaultValue.toString()));
		return (T) getTypedValue(value, defaultValue.getClass());
	}

	protected void safeUpdateEventCreated(ZonedDateTime value) {
		safeAddReplaceParameter(EVENT_CREATED, value.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
		safeAddReplaceParameter(EVENT_TIMESTAMP, Long.toString(value.toEpochSecond()));
	}

	protected void safeUpdateInternalParameter(String key, String value) {
		safeAddReplaceTypedParameter(key, value);
	}

	protected <T> void safeUpdateInternalTypedParameter(String key, T value) {
		safeAddReplaceTypedParameter(key, value);
	}

	@Override
	public void setAppName(String value) {
		safeAddReplaceParameter(APP_NAME, value);
	}

	@Override
	public void setConversationId(String value) {
		safeAddReplaceParameter(CONVERSATION_ID, value);

	}

	@Override
	public void setDivision(String value) {
		safeAddReplaceParameter(DIVISION, value);

	}

	@Override
	public void setEnv(String value) {
		safeAddReplaceParameter(ENV, value);

	}

	@Override
	public void setEventCategory(String value) {
		safeAddReplaceParameter(EVENT_CATEGORY, value);

	}

	@Override
	public void setEventLevel(AuditEventLevel value) {
		safeAddReplaceParameter(EVENT_LEVEL, value.toString());

	}

	@Override
	public void setEventName(String value) {
		safeAddReplaceParameter(EVENT_NAME, value);

	}

	@Override
	public void setEventSource(String value) {
		safeAddReplaceParameter(EVENT_SOURCE, value);

	}

	@Override
	public void setEventStatus(AuditEventStatus value) {
		safeAddReplaceParameter(EVENT_STATUS, value.toString());

	}

	@Override
	public void setEventStatusDescription(String value) {
		safeAddReplaceParameter(EVENT_STATUS_DESCRIPTION, value);

	}

	@Override
	public void setEventType(String value) {
		safeAddReplaceParameter(EVENT_TYPE, value);

	}

	@Override
	public void setEventVersion(String value) {
		safeAddReplaceParameter(EVENT_VERSION, value);

	}

	@Override
	public void setExperienceId(String value) {
		safeAddReplaceParameter(EXPERIENCE_ID, value);

	}

	@Override
	public void setMachine(String value) {
		safeAddReplaceParameter(MACHINE, value);

	}

	@Override
	public void setMessage(String value) {
		safeAddReplaceParameter(MESSAGE, value);

	}

	@Override
	public void setModuleName(String value) {
		safeAddReplaceParameter(MODULE_NAME, value);

	}

	@Override
	public void setNamespace(String value) {
		safeAddReplaceParameter(NAMESPACE, value);

	}

	@Override
	public void setPartnerId(String value) {
		safeAddReplaceParameter(PARTNER_ID, value);
	}

	@Override
	public void setRegion(String value) {
		safeAddReplaceParameter(REGION, value);

	}

	@Override
	public void setSequenceId(String value) {
		safeAddReplaceParameter(SEQUENCE_ID, value);

	}

	@Override
	public void setSessionId(String value) {
		safeAddReplaceParameter(SESSION_ID, value);

	}

	@Override
	public void setTenantId(String value) {
		safeAddReplaceParameter(TENANT_ID, value);

	}

	@Override
	public void setTransactionId(String value) {
		safeAddReplaceParameter(TRANSACTION_ID, value);

	}

	protected String specialFunctionBoolean(boolean value) {
		return specialFunctionBuilderBoolean(Boolean.toString(value));
	}

	private String specialFunctionBuilderBoolean(String value) {
		return "$Boolean(" + value + ")";
	}

	private String specialFunctionBuilderNumber(String value) {
		return "$Number(" + value + ")";
	}

	protected String specialFunctionNumber(double value) {
		return specialFunctionBuilderNumber(Double.toString(value));
	}

	protected String specialFunctionNumber(float value) {
		return specialFunctionBuilderNumber(Float.toString(value));
	}

	protected String specialFunctionNumber(int value) {
		return specialFunctionBuilderNumber(Integer.toString(value));
	}

	protected String specialFunctionNumber(long value) {
		return specialFunctionBuilderNumber(Long.toString(value));
	}

	@Override
	public String toJson() {
		return toJson(false);
	}

	@Override
	public String toJson(boolean prettyPrint) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, prettyPrint);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

			objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
			String json = objectMapper.writeValueAsString(parameters);
			return transformSpecialFunctionsJson(json);

		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public String toString() {
		return toJson();
	}

	private String transformSpecialFunctionsJson(String input) {
		// Look for transform functions.. $Number() and $Boolean()
		try {
			final String regex = "\"\\$(Number|Boolean)\\((.*?)\\)\"";
			final String subst = "$2";
			final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			final Matcher matcher = pattern.matcher(input);
			return matcher.replaceAll(subst);
		} catch (Exception ex) {
			return input;
		}
	}

	private String transformSpecialFunctionsParameter(String input) {
		if (input.contains("$Number(") || input.contains("$Boolean")) {
			return transformSpecialFunctionsJson("\"" + input + "\"");
		}
		return input;
	}

}
