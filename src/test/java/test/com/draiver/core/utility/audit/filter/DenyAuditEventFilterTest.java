package test.com.draiver.core.utility.audit.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.filter.AuditEventFilter;
import com.draiver.core.utility.audit.filter.DenyAuditEventFilter;

import test.com.draiver.core.utility.audit.events.EventUtils;

class DenyAuditEventFilterTest {

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
	void testDenyAuditEventFilter() {
		AuditEventFilter eventFilter = null;

		// filter by EventName. Exact Match
		eventFilter = new DenyAuditEventFilter("EventName", "EVENT1");
		assertFalse(eventFilter.isAllow());
		assertTrue(eventFilter.isMatch(_event1));
		assertFalse(eventFilter.isMatch(_event2));

		// filter by EventName. Exact Match Case insenstive
		eventFilter = new DenyAuditEventFilter("EventName", "event1");
		assertTrue(eventFilter.isMatch(_event1));
		assertFalse(eventFilter.isMatch(_event2));

		// filter by EventName. Regex
		eventFilter = new DenyAuditEventFilter("EventName", "EVENT.*");
		assertTrue(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by EventName. Allow All
		eventFilter = new DenyAuditEventFilter("EventName", ".*");
		assertTrue(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by EventName. Deny anything ending in 2
		eventFilter = new DenyAuditEventFilter("EventName", ".*?2");
		assertFalse(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by Machine. Exact Match
		eventFilter = new DenyAuditEventFilter("Machine", EventUtils.getMachineName());
		assertTrue(eventFilter.isMatch(_event1));
		assertTrue(eventFilter.isMatch(_event2));

		// filter by property not included in event. Should not be denied
		eventFilter = new DenyAuditEventFilter("BadProperty", "derp");
		assertFalse(eventFilter.isMatch(_event1));
		assertFalse(eventFilter.isMatch(_event2));

		// filter by JSON
		eventFilter = new DenyAuditEventFilter("\"EventName\":\"EVENT1\"");
		assertFalse(eventFilter.isAllow());
		assertTrue(eventFilter.isMatch(_event1));
		assertFalse(eventFilter.isMatch(_event2));

		// filter by JSON (no match)
		eventFilter = new DenyAuditEventFilter("\"EventName\":\"NO_MATCH\"");
		assertFalse(eventFilter.isAllow());
		assertFalse(eventFilter.isMatch(_event1));

	}

}