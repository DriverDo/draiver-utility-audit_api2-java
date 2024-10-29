package test.com.draiver.core.utility.audit.auditor;

import com.draiver.core.utility.audit.appender.InMemoryAuditAppender;
import com.draiver.core.utility.audit.auditor.StandardAuditor;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class StandardAuditorTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testStandardAuditor() throws InterruptedException {
		StandardAuditor auditor = new StandardAuditor();
		InMemoryAuditAppender appender1 = new InMemoryAuditAppender();

		auditor.getAppenders().add(appender1);

		for (int i = 0; i < 10; i++) {
			auditor.auditAsync(new GenericAuditEvent("EVENT" + i));
		}
		
		Thread.sleep(1000);

		assertEquals(10, appender1.getMessages().size());
		assertTrue(appender1.getMessages().get(0).contains("testStandardAuditor"));
		AuditEvent tmpEvent = AuditEventUtils.fromJson(appender1.getMessages().get(0));
		assertNotNull(tmpEvent);
		assertTrue(tmpEvent.getEventSource().contains("testStandardAuditor"));

	}
	
	@Test
	void testStandardAuditorListOfEvents() throws InterruptedException {
		StandardAuditor auditor = new StandardAuditor();
		InMemoryAuditAppender appender1 = new InMemoryAuditAppender();

		auditor.getAppenders().add(appender1);

		List<AuditEvent> auditEvents = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			auditEvents.add(new GenericAuditEvent("EVENT" + i));
		}

		auditor.auditAsync(auditEvents);
		Thread.sleep(1000);
		
		assertEquals(10, appender1.getMessages().size());
		assertTrue(appender1.getMessages().get(0).contains("testStandardAuditorListOfEvents"));
		AuditEvent tmpEvent = AuditEventUtils.fromJson(appender1.getMessages().get(0));
		assertNotNull(tmpEvent);
		assertTrue(tmpEvent.getEventSource().contains("testStandardAuditorListOfEvents"));

	}
	
	@Test
	void testThreads() {
		List<CompletableFuture<?>> futures = new ArrayList<>();
		for(int i=0; i<100; i++) {
			futures.add(CompletableFuture.runAsync(new SampleThread()));		
		}
		try {
			CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
			
		} catch (Exception e) {
			fail(e);
		}
		
		
	}

}
