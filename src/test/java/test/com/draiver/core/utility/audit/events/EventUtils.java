package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.platform.commons.util.StringUtils;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.EventConfigImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventUtils {

	protected static final String UNKNOWN = "Unknown";

	public static final String APP_NAME = "App 1";
	public static final String CONVERSATION_ID = UUID.randomUUID().toString();
	public static final String DIVISION = "Divsion 1";
	public static final String ENV = "DEV";
	public static final ZonedDateTime ENV_CREATED = ZonedDateTime.now();
	public static final AuditEventLevel EVENT_LEVEL = AuditEventLevel.INFO;
	public static final AuditEventStatus EVENT_STATUS = AuditEventStatus.SUCCESS;
	public static final String EVENT_STATUS_DESCRIPTION = "description";
	public static final String EVENT_VERSION = "1.0";
	public static final String EVENT_TYPE = "GenericAuditEvent";
	public static final String EXPERIENCE_ID = UUID.randomUUID().toString();
	public static final String MACHINE = getMachineName();
	public static final String MESSAGE = "message";
	public static final String MODULE_NAME = "module";
	public static final String NAMESPC = "namespace";
	public static final String SESSION_ID = UUID.randomUUID().toString();
	public static final String SEQUENCE_ID = UUID.randomUUID().toString();
	public static final String TRANSACTION_ID = UUID.randomUUID().toString();
	public static final String EVENT_SOURCE = "event source";

	private EventUtils() {

	}

	public static EventConfig createEventConfig() {
		EventConfig eventConfig = new EventConfigImpl();
		eventConfig.setAppName(APP_NAME);
		eventConfig.setConversationId(CONVERSATION_ID);
		eventConfig.setDivision(DIVISION);
		eventConfig.setEnv(ENV);
		eventConfig.setExperienceId(EXPERIENCE_ID);
		eventConfig.setMachine(MACHINE);
		eventConfig.setModuleName(MODULE_NAME);
		eventConfig.setNamespace(NAMESPC);
		eventConfig.setSequenceId(SEQUENCE_ID);
		eventConfig.setSessionId(SESSION_ID);
		eventConfig.setTransactionId(TRANSACTION_ID);
		return eventConfig;
	}

	public static void assertEventConfigValid(AuditEvent auditEvent) {
		assertEventConfigValid(auditEvent, EventUtils.createEventConfig());
	}

	public static void assertEventConfigValid(AuditEvent auditEvent, EventConfig eventConfig) {
		try {
			Map<String, String> beanEventConfig = BeanUtils.describe(eventConfig);
			for (String key : beanEventConfig.keySet()) {
				if (key.equals("class")) {
					continue;
				}
				assertEquals(BeanUtils.getProperty(eventConfig, key), BeanUtils.getProperty(auditEvent, key), "Problem with key " + key);
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			fail(e);
		}
	}

	public static void assertAuditEventValid(AuditEvent auditEvent) {
		assertEventConfigValid(auditEvent);
		assertNotNull(auditEvent.getEventCreated());
		assertNotNull(auditEvent.getEventId());
		assertTrue(isValidJson(auditEvent.toJson()));
	}

	public static void assertAuditEventValid(AuditEvent auditEvent, EventConfig eventConfig) {
		assertEventConfigValid(auditEvent, eventConfig);
		assertNotNull(auditEvent.getEventCreated());
		assertNotNull(auditEvent.getEventId());
	}

	public static boolean isValidJson(String json) {
		try {
			Map<String, String> map = new TreeMap<String, String>();
			fromJson(json, map.getClass());
			if (json.contains("$Number(") || json.contains("$Boolean(")) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static <T> T fromJson(String json, Class<T> type) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

		if (type == null || StringUtils.isBlank(json)) {
			return null;
		}

		try {
			return objectMapper.readValue(json, type);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String getMachineName() {
		try {
			return java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return UNKNOWN;
		}
	}

}
