package test.com.draiver.core.utility.audit.appender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.appender.InMemoryAuditAppender;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.exception.AuditException;

class InMemoryAuditAppenderTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testInMemoryAuditAppender() throws AuditException {
		
		InMemoryAuditAppender appender = new InMemoryAuditAppender(10);
		appender.append(new GenericAuditEvent("EVENT0", "TEST", AuditEventLevel.FATAL, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT1", "TEST", AuditEventLevel.ERROR, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT2", "TEST", AuditEventLevel.WARN, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT3", "TEST", AuditEventLevel.INFO, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT4", "TEST", AuditEventLevel.DEBUG, AuditEventStatus.SUCCESS));

		assertEquals(5, appender.getMessages().size());
		for (int i = 0; i < appender.getMessages().size(); i++) {
			assertTrue(appender.getMessages().get(i).contains("EVENT" + i));
		}
		
		appender = new InMemoryAuditAppender(2);
		appender.append(new GenericAuditEvent("EVENT0", "TEST", AuditEventLevel.FATAL, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT1", "TEST", AuditEventLevel.ERROR, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT2", "TEST", AuditEventLevel.WARN, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT3", "TEST", AuditEventLevel.INFO, AuditEventStatus.SUCCESS));
		appender.append(new GenericAuditEvent("EVENT4", "TEST", AuditEventLevel.DEBUG, AuditEventStatus.SUCCESS));

		assertEquals(2, appender.getMessages().size());
		assertTrue(appender.getMessages().get(0).contains("EVENT3"));
		assertTrue(appender.getMessages().get(1).contains("EVENT4"));

	}

}
