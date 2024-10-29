package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.MapAuditEvent;

class MapAuditEventTest {

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
	void testConstructorJson() {
		AuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2,_eventConfig);
		AuditEvent event2 = new MapAuditEvent(event1.toJson());
								
		_testConstructor(event1, event2);
	}
	
	@Test
	void testConstructorJsonEventConfig() {
		AuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2);
		AuditEvent event2 = new MapAuditEvent(_eventConfig, event1.toJson());
								
		_testConstructor(event1, event2);
	}
	
	@Test
	void testConstructorMap() {
		AuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2,_eventConfig);
		AuditEvent event2 = new MapAuditEvent(event1.getParameters());
								
		_testConstructor(event1, event2);
	}
	
	@Test
	void testConstructorMapEventConfig() {
		AuditEvent event1 = new SampleAuditEvent(PARAMETER1, PARAMETER2);
		AuditEvent event2 = new MapAuditEvent(_eventConfig, event1.getParameters());
								
		_testConstructor(event1, event2);
	}
	
	private static void _testConstructor(AuditEvent event1, AuditEvent event2) {
		EventUtils.isValidJson(event2.toJson());
		EventUtils.assertAuditEventValid(event2);
		String json = event2.toJson();
		assertTrue(json.contains("X-param1"));
		assertTrue(json.contains("X-param2"));
		assertTrue(json.contains(PARAMETER1));
		assertTrue(json.contains(PARAMETER2));
		assertEquals(event1.getClass().getSimpleName(), event2.getEventType());
		assertEquals(event1.getClass().getSimpleName(), event2.getEventName());
		assertEquals(event1.getEventId(),event2.getEventId());
		assertEquals(event1.getEventTimestamp(),event2.getEventTimestamp());
		assertEquals(event1.getEventCreated(),event2.getEventCreated());
	}
	
	@Test
	void testSpecialFunctions() {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("param1","abc");
		parameters.put("param2","def");
		parameters.put("param3","$Number(69)");
		parameters.put("param4","$Number(69.69)");
		parameters.put("param5","$Number(-69)");
		parameters.put("param6","$Boolean(true)");
		parameters.put("param7", new SampleAuditEvent(PARAMETER1, PARAMETER2).toJson());
		
		MapAuditEvent auditEvent = new MapAuditEvent(parameters);
		assertNotNull(auditEvent);
		
		String json = auditEvent.toJson();
		System.out.println(json);
		EventUtils.isValidJson(json);		
	}

}
