package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.StopwatchAuditEvent;

@SuppressWarnings("squid:S2925")
class StopwatchAuditEventTest {

	@Test
	void testGetterSetters() {
		ZonedDateTime startDate = ZonedDateTime.now();
		ZonedDateTime stopDate = startDate.plusMinutes(5);

		StopwatchAuditEvent event1 = new StopwatchAuditEvent(startDate);
		event1.setStopTime(stopDate);
		assertFalse(event1.getDurationMs() == 0);
		assertEquals(300000, event1.getDurationMs());		

	}

	@Test
	void testStop() throws InterruptedException {
		StopwatchAuditEvent event1 = new StopwatchAuditEvent();
		event1.setEventSource("test");
		Thread.sleep(2000);
		event1.stop();
		assertFalse(event1.getDurationMs() == 0);
		assertTrue(event1.getDurationMs() > 1000 && event1.getDurationMs() < 3000);
		assertTrue(event1.getMessage().startsWith("Source"));
		System.out.println(event1.toJson());
	}
	
	@Test
	void testJson() throws InterruptedException {
		StopwatchAuditEvent event1 = new StopwatchAuditEvent();
		Thread.sleep(2000);
		assertFalse(event1.getDurationMs() == 0);
		assertTrue(event1.getDurationMs() > 1000 && event1.getDurationMs() < 3000);
		String json = event1.toJson();
		System.out.println(json);
		assertTrue(json.contains("X-DurationMs"));
	}

}
