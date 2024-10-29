package test.com.draiver.core.utility.audit.masker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.filter.AllowAuditEventFilter;
import com.draiver.core.utility.audit.filter.DenyAuditEventFilter;
import com.draiver.core.utility.audit.masker.AuditEventMasker;

class JsonOnlyAuditEventMaskerBaseTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFilters() {
		AuditEventMasker masker = new SimpleAuditEventMasker();
		masker.getFilters().add(new AllowAuditEventFilter("EventName", "EVENT[1|3]"));

		String json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertTrue(json.contains("****"));

		json = masker.maskToJson(new GenericAuditEvent("EVENT2"));
		assertFalse(json.contains("****"));

		json = masker.maskToJson(new GenericAuditEvent("EVENT3"));
		assertTrue(json.contains("****"));

		masker = new SimpleAuditEventMasker();
		masker.getFilters().add(new DenyAuditEventFilter("EventName", "EVENT[1|3]"));

		json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertTrue(!json.contains("****"));

		json = masker.maskToJson(new GenericAuditEvent("EVENT2"));
		assertTrue(json.contains("****"));

		json = masker.maskToJson(new GenericAuditEvent("EVENT3"));
		assertTrue(!json.contains("****"));
	}

	@Test
	void testShouldMask() {
		AuditEventMasker masker = new SimpleAuditEventMasker();
		masker.getFilters().add(new AllowAuditEventFilter("EventName", "EVENT[1|3]"));

		assertTrue(masker.shouldMask(new GenericAuditEvent("EVENT1")));
		assertTrue(!masker.shouldMask(new GenericAuditEvent("EVENT2")));
		assertTrue(masker.shouldMask(new GenericAuditEvent("EVENT3")));
		assertTrue(!masker.shouldMask(new GenericAuditEvent("EVENT4")));

		masker = new SimpleAuditEventMasker();
		masker.getFilters().add(new DenyAuditEventFilter("EventName", "EVENT[1|3]"));

		assertTrue(!masker.shouldMask(new GenericAuditEvent("EVENT1")));
		assertTrue(masker.shouldMask(new GenericAuditEvent("EVENT2")));
		assertTrue(!masker.shouldMask(new GenericAuditEvent("EVENT3")));
		assertTrue(masker.shouldMask(new GenericAuditEvent("EVENT4")));
	}

}
