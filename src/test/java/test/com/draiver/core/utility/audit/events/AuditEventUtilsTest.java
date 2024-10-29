package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.EventConfig;

class AuditEventUtilsTest {

	private static final EventConfig _eventConfig = EventUtils.createEventConfig();
	private static final String PARAMETER1 = "param1";
	private static final String PARAMETER2 = "param2";

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCloneEventConfig() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		EventConfig ec1 = EventUtils.createEventConfig();
		EventConfig ec2 = AuditEventUtils.clone(ec1);
		assertNotNull(ec2);
		
		Map<String, String> defEc1 = BeanUtils.describe(ec1);
		for(String key : defEc1.keySet()) {
			if (key.equals("class")) { continue;}
			assertEquals(BeanUtils.getProperty(ec1, key),BeanUtils.getProperty(ec2, key));
		}
		
		ec2.setAppName("JASON");
		assertNotEquals(ec1.getAppName(),ec2.getAppName());
		
	}
	
	@Test
	void testCloneAuditEvent() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		SampleAuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2, EventUtils.createEventConfig());
		SampleAuditEvent event2 = AuditEventUtils.clone(event1);
		
		List<String> specialKeys = new ArrayList<>();
		specialKeys.add("class");
		specialKeys.add("eventCreated");
		specialKeys.add("eventId");
		specialKeys.add("eventTimestamp");
		specialKeys.add("parameters");
		
		Map<String, String> defEvent1 = BeanUtils.describe(event1);
		for(String key : defEvent1.keySet()) {
			if(specialKeys.contains(key)) {continue;}
			assertEquals(BeanUtils.getProperty(event1, key),BeanUtils.getProperty(event2, key));
		}		
		
		event2.setParameter1("JASON");		
		assertNotEquals(event1.getParameter1(),event2.getParameter2());
		
		
	}

	@Test
	void testFromJson() {
		AuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2, _eventConfig);
		AuditEvent event2 = AuditEventUtils.fromJson(event1.toJson());
		_testFromJson(event1, event2);
	}

	@Test
	void testFromJsonEventConfig() {
		AuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2);
		AuditEvent event2 = AuditEventUtils.fromJson(_eventConfig, event1.toJson());
		_testFromJson(event1, event2);
	}

	private static void _testFromJson(AuditEvent event1, AuditEvent event2) {
		EventUtils.isValidJson(event2.toJson());
		EventUtils.assertAuditEventValid(event2);
		String json = event2.toJson();
		assertTrue(json.contains("X-param1"));
		assertTrue(json.contains("X-param2"));
		assertTrue(json.contains(PARAMETER1));
		assertTrue(json.contains(PARAMETER2));
		assertEquals(event1.getClass().getSimpleName(), event2.getEventType());
		assertEquals(event1.getClass().getSimpleName(), event2.getEventName());
		assertEquals(event1.getEventId(), event2.getEventId());
		assertEquals(event1.getEventTimestamp(), event2.getEventTimestamp());
		assertEquals(event1.getEventCreated(), event2.getEventCreated());
	}

	@SuppressWarnings("squid:S2925")
	@Test
	void testPopulateAuditEventNoOverride() throws InterruptedException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		SampleAuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2, _eventConfig);		
		Thread.sleep(1000);
		SampleAuditEvent event2 = new SampleAuditEvent();
		event2.setAppName("App 2");
		event2.setExperienceId("Exp1");

		AuditEventUtils.populateAuditEvent(event1, event2, false);

		assertEquals(event1.getParameter1(), event2.getParameter1());
		assertEquals(event1.getParameter1(), event2.getParameter1());
		assertEquals(event1.getConversationId(), event2.getConversationId());

		assertNotEquals(event1.getAppName(), event2.getAppName());
		assertNotEquals(event1.getEventId(), event2.getEventId());
		assertNotEquals(event1.getEventCreated().toString(), event2.getEventCreated().toString());
		assertNotEquals(event1.getEventTimestamp(), event2.getEventTimestamp());
		assertNotEquals(event1.getExperienceId(), event2.getExperienceId());

	}

	@Test
	void testPopulateAuditEventOverride() throws InterruptedException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		SampleAuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2, _eventConfig);
		Thread.sleep(1000); //NOSONAR
		SampleAuditEvent event2 = new SampleAuditEvent();
		event2.setAppName("App 2");
		event2.setExperienceId("Exp1");

		AuditEventUtils.populateAuditEvent(event1, event2, true);

		assertEquals(event1.getParameter1(), event2.getParameter1());
		assertEquals(event1.getParameter1(), event2.getParameter1());
		assertEquals(event1.getConversationId(), event2.getConversationId());

		assertEquals(event1.getAppName(), event2.getAppName());
		assertEquals(event1.getExperienceId(), event2.getExperienceId());

		assertNotEquals(event1.getEventId(), event2.getEventId());
		assertNotEquals(event1.getEventCreated().toString(), event2.getEventCreated().toString());
		assertNotEquals(event1.getEventTimestamp(), event2.getEventTimestamp());

	}

	@Test
	void testPopulateAuditEventByEventConfigOverride() {
		SampleAuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2);
		event1.setAppName("App 2");

		AuditEventUtils.populateAuditEvent(_eventConfig, event1, true);
		assertEquals(_eventConfig.getAppName(), event1.getAppName());
		assertEquals("App 1", event1.getAppName());

	}

	@Test
	void testPopulateAuditEventByEventConfigNoOverride() {
		SampleAuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2);
		event1.setAppName("App 2");

		AuditEventUtils.populateAuditEvent(_eventConfig, event1, false);
		assertNotEquals(_eventConfig.getAppName(), event1.getAppName());
		assertEquals("App 2", event1.getAppName());

	}

	@Test
	void testFetchCurrentEventSource() {
		String eventSource = AuditEventUtils.fetchCurrentEventSource();
		assertTrue(eventSource.contains("testFetchCurrentEventSource"));
	}

}
