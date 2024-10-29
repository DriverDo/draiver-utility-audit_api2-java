package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.GenericAuditEvent;

class GenericAuditEventTest {

	private static String EVENT_NAME = "GENERIC_EVENT_1";
	private static String STRING_VALUE_1 = "Value 1";

	private GenericAuditEvent _event1;
	private EventConfig _eventConfig = EventUtils.createEventConfig();

	@BeforeEach
	void setUp() throws Exception {
		_event1 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGenericAuditEventString() {
		assertNotNull(_event1);
		assertEquals(EVENT_NAME, _event1.getEventName());
		EventUtils.assertAuditEventValid(_event1);
		assertEquals(AuditEventLevel.INFO, _event1.getEventLevel());
		assertEquals(AuditEventStatus.SUCCESS, _event1.getEventStatus());
	}

	@Test
	void testGenericAuditEventStringAuditEventLevelAuditEventStatus() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, AuditEventLevel.DEBUG, AuditEventStatus.TRANSACTION_BEGIN);
		assertEquals(EVENT_NAME, event2.getEventName());
		assertEquals(AuditEventLevel.DEBUG, event2.getEventLevel());
		assertEquals(AuditEventStatus.TRANSACTION_BEGIN, event2.getEventStatus());
	}

	@Test
	void testGenericAuditEventStringEventConfig() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(EVENT_NAME, event2.getEventName());
	}

	@Test
	void testGenericAuditEventStringEventConfigAuditEventLevelAuditEventStatus() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig, AuditEventLevel.DEBUG, AuditEventStatus.TRANSACTION_BEGIN);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(EVENT_NAME, event2.getEventName());
		assertEquals(AuditEventLevel.DEBUG, event2.getEventLevel());
		assertEquals(AuditEventStatus.TRANSACTION_BEGIN, event2.getEventStatus());
	}

	@Test
	void testGenericAuditEventStringString() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, EventUtils.EVENT_TYPE);
		assertEquals(EVENT_NAME, event2.getEventName());
		assertEquals(EventUtils.EVENT_TYPE, event2.getEventType());
		assertEquals(AuditEventLevel.INFO, event2.getEventLevel());
		assertEquals(AuditEventStatus.SUCCESS, event2.getEventStatus());
	}

	@Test
	void testGenericAuditEventStringStringAuditEventLevelAuditEventStatus() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, EventUtils.EVENT_TYPE, AuditEventLevel.DEBUG, AuditEventStatus.TRANSACTION_BEGIN);
		assertEquals(EVENT_NAME, event2.getEventName());
		assertEquals(EventUtils.EVENT_TYPE, event2.getEventType());
		assertEquals(AuditEventLevel.DEBUG, event2.getEventLevel());
		assertEquals(AuditEventStatus.TRANSACTION_BEGIN, event2.getEventStatus());
	}

	@Test
	void testGenericAuditEventStringStringEventConfig() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, EventUtils.EVENT_TYPE, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(EVENT_NAME, event2.getEventName());
		assertEquals(EventUtils.EVENT_TYPE, event2.getEventType());
		assertEquals(AuditEventLevel.INFO, event2.getEventLevel());
		assertEquals(AuditEventStatus.SUCCESS, event2.getEventStatus());
	}

	@Test
	void testGenericAuditEventStringStringEventConfigAuditEventLevelAuditEventStatus() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, EventUtils.EVENT_TYPE, _eventConfig, AuditEventLevel.DEBUG, AuditEventStatus.TRANSACTION_BEGIN);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(EVENT_NAME, event2.getEventName());
		assertEquals(EventUtils.EVENT_TYPE, event2.getEventType());
		assertEquals(AuditEventLevel.DEBUG, event2.getEventLevel());
		assertEquals(AuditEventStatus.TRANSACTION_BEGIN, event2.getEventStatus());
	}

	@Test
	void testAppName() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setAppName(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getAppName());
	}

	@Test
	void testConversationId() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setConversationId(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getConversationId());
	}

	@Test
	void testGetDivision() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setDivision(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getDivision());
	}

	@Test
	void testGetEnv() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setEnv(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getEnv());
	}

	@Test
	void testGetEventCategory() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setEventCategory(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getEventCategory());
	}

	@Test
	void testGetEventCreated() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertNotNull(event2.getEventCreated());
	}

	@Test
	void testGetEventId() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertNotNull(event2.getEventId());
	}

	@Test
	void testGetEventLevel() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(AuditEventLevel.INFO, event2.getEventLevel());
	}

	@Test
	void testGetEventName() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(EVENT_NAME, event2.getEventName());
		event2.setEventName(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getEventName());
	}

	@Test
	void testGetEventSource() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setEventSource(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getEventSource());
	}

	@Test
	void testGetEventStatus() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(AuditEventStatus.SUCCESS, event2.getEventStatus());
		event2.setEventStatus(AuditEventStatus.FAIL);
		assertEquals(AuditEventStatus.FAIL, event2.getEventStatus());
	}

	@Test
	void testGetEventStatusDescription() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setEventStatusDescription(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getEventStatusDescription());
	}

	@Test
	void testGetEventTimestamp() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME, _eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertNotNull(Long.valueOf(event2.getEventTimestamp()));
	}

	@Test
	void testGetEventType() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setEventType(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getEventType());
	}

	@Test
	void testGetEventVersion() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME);		
		assertEquals("1.0",event2.getEventVersion());
		
		event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setEventVersion(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getEventVersion());
	}

	@Test
	void testGetExperienceId() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setExperienceId(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getExperienceId());
	}

	@Test
	void testGetMachine() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setMachine(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getMachine());
	}

	@Test
	void testGetMessage() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setMessage(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getMessage());
	}

	@Test
	void testGetModuleName() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setModuleName(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getModuleName());
	}

	@Test
	void testGetNamespace() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setNamespace(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getNamespace());
	}

	@Test
	void testGetParameters() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		assertEquals(26, event2.getParameters().size());
	}

	@Test
	void testGetSequenceId() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setSequenceId(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getSequenceId());
	}

	@Test
	void testGetSessionId() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setSessionId(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getSessionId());
	}

	@Test
	void testGetTransactionId() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		event2.setTransactionId(STRING_VALUE_1);
		assertEquals(STRING_VALUE_1, event2.getTransactionId());
	}

	@Test
	void testToJson() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		EventUtils.isValidJson(event2.toJson());
	}

	@Test
	void testToJsonBoolean() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		EventUtils.isValidJson(event2.toJson(true));
		EventUtils.isValidJson(event2.toJson(false));
	}

	@Test
	void testToString() {
		AuditEvent event2 = new GenericAuditEvent(EVENT_NAME,_eventConfig);
		EventUtils.assertAuditEventValid(event2, _eventConfig);
		EventUtils.isValidJson(event2.toString());
	}

}
