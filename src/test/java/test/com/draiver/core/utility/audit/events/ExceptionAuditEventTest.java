package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.ExceptionAuditEvent;

class ExceptionAuditEventTest {

	private EventConfig _eventConfig = EventUtils.createEventConfig();

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testConstructor() {
		ExceptionAuditEvent event1 = new ExceptionAuditEvent(new NullPointerException("TEST1"), _eventConfig);
		EventUtils.assertAuditEventValid(event1, _eventConfig);
		
		assertEquals(AuditEventLevel.ERROR, event1.getEventLevel());
		assertEquals(AuditEventStatus.FAIL, event1.getEventStatus());
		assertEquals("ExceptionAuditEvent",event1.getEventType());
		assertEquals("1.0",event1.getEventVersion());
		assertTrue(event1.getException().startsWith("java.lang.NullPointerException: TEST1"));
		assertNotNull(event1.getExceptionSource());
		assertNotNull(event1.getStackTrace());
		assertTrue(event1.getExceptionSource().startsWith("test.com.draiver.core.utility.audit.events.ExceptionAuditEventTest.testConstructor"));
		assertTrue(event1.getStackTrace().startsWith("[test.com.draiver.core.utility.audit.events.ExceptionAuditEventTest.testConstructor(ExceptionAuditEventTest.java"));

	}

}
