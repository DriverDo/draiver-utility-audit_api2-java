package test.com.draiver.core.utility.audit.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.filter.AllowAuditEventFilter;
import com.draiver.core.utility.audit.filter.AuditEventFilter;

import test.com.draiver.core.utility.audit.events.EventUtils;

class AllowAuditEventFilterTest {

	private GenericAuditEvent _event1;
	private GenericAuditEvent _event2;
	private EventConfig _eventConfig = EventUtils.createEventConfig();

	@BeforeEach
	void setUp() throws Exception {

		_event1 = new GenericAuditEvent("EVENT1", _eventConfig);
		_event2 = new GenericAuditEvent("EVENT2", _eventConfig);

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testAllowAuditEventFilter() {
		AuditEventFilter eventFilter = null;

		// filter by EventName. Exact Match
		eventFilter = new AllowAuditEventFilter("EventName", "EVENT1");
		assertTrue(eventFilter.isAllow());
		assertTrue(eventFilter.isMatch(_event1));
		assertFalse(eventFilter.isMatch(_event2));

		// filter by EventName. Exact Match Case insenstive
		eventFilter = new AllowAuditEventFilter("EventName", "event1");
		assertTrue(eventFilter.isMatch(_event1));
		assertFalse(eventFilter.isMatch(_event2));

		// filter by EventName. Regex
		eventFilter = new AllowAuditEventFilter("EventName", "EVENT.*");
		assertTrue(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by EventName. Allow All
		eventFilter = new AllowAuditEventFilter("EventName", ".*");
		assertTrue(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by EventName. Deny anything ending in 2
		eventFilter = new AllowAuditEventFilter("EventName", ".*?2");
		assertFalse(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by Machine. Exact Match
		eventFilter = new AllowAuditEventFilter("Machine", EventUtils.getMachineName());
		assertTrue(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by bad Property. Bad Properties should be allowed as a match
		eventFilter = new AllowAuditEventFilter("BadProperty", "derp");
		assertTrue(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// Filter by JSON pattern
		eventFilter = new AllowAuditEventFilter("\"EventName\":\"EVENT1\"");
		assertTrue(eventFilter.isAllow());
		assertTrue(eventFilter.isMatch(_event1));
		assertFalse(eventFilter.isMatch(_event2));

		// Filter by JSON pattern (no match)
		eventFilter = new AllowAuditEventFilter("\"EventName\":\"NO_MATCH\"");
		assertTrue(eventFilter.isAllow());
		assertFalse(eventFilter.isMatch(_event1));

	}

}