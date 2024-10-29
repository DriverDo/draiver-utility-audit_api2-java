package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.EventConfig;

class AuditEventBaseTest {

	private SampleAuditEvent _event1;
	private EventConfig _eventConfig;

	@BeforeEach
	void setUp() throws Exception {
		_eventConfig = EventUtils.createEventConfig();

		_event1 = new SampleAuditEvent();
		_event1.setParameter1("value1");
		_event1.setParameter2("value2");
		_event1.setParameter3(69);
		_event1.setParameter4(true);

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testConstructors() {
		assertEquals(30, _event1.getParameters().keySet().size());
		assertEquals("value1", _event1.getParameter1());
		assertEquals("value2", _event1.getParameter2());
		assertEquals(69, _event1.getParameter3());
		assertEquals(true, _event1.getParameter4());

		AuditEvent event2 = new SampleAuditEvent(_eventConfig);
		EventUtils.assertAuditEventValid(event2);		
		
		assertEquals(AuditEventLevel.INFO,_event1.getEventLevel());
		assertEquals(AuditEventStatus.SUCCESS,_event1.getEventStatus());
	}

	@Test
	void testToString() {
		assertTrue(EventUtils.isValidJson(_event1.toString()));
	}
	
	@Test
	void testToJson() {		
		System.out.println(_event1.toJson());
		
		assertTrue(EventUtils.isValidJson(_event1.toJson()));
		assertTrue(EventUtils.isValidJson(_event1.toJson(true)));
		assertTrue(EventUtils.isValidJson(_event1.toJson(false)));
		
		SampleAuditEvent tmpEvent1 = new SampleAuditEvent();
		tmpEvent1.setParameter1("value-abc");
		tmpEvent1.setParameter2("value-def");		
		
		SampleAuditEvent tmpEvent2 = new SampleAuditEvent();
		tmpEvent2.setParameter1(tmpEvent1.toJson());
		
		assertTrue(EventUtils.isValidJson(tmpEvent2.toJson()));
		assertTrue(EventUtils.isValidJson(tmpEvent2.toJson(true)));
		assertTrue(EventUtils.isValidJson(tmpEvent2.toJson(false)));	
		
		// Test Special Functions Translation
		
	}
	

	

}
