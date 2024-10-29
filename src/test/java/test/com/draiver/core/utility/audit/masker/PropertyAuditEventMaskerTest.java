package test.com.draiver.core.utility.audit.masker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.masker.AuditEventMasker;
import com.draiver.core.utility.audit.masker.PropertyAuditEventMasker;

class PropertyAuditEventMaskerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testPropertyAuditEventMasker() {
		PropertyAuditEventMasker masker = new PropertyAuditEventMasker("EventName", "****");

		String json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertFalse(json.contains("EVENT1"));
		assertNotNull(AuditEventUtils.fromJson(json));

		masker.setPropertyName("ConversationId");
		json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertTrue(json.contains("****"));

		masker.setPropertyName("INFO");
		json = masker.maskToJson(new GenericAuditEvent("EVENT1", AuditEventLevel.INFO, AuditEventStatus.SUCCESS));
		assertFalse(json.contains("****"));

		masker.setPropertyName("Message");
		GenericAuditEvent event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("THIS WORKS!");
		json = masker.maskToJson(event1);
		assertTrue(json.contains("****"));
		assertFalse(json.contains("THIS WORKS!"));

		masker.setPropertyName("Message");
		masker.setRegExPattern("\\d{3}-\\d{2}-\\d{4}");
		masker.setReplaceWith("!!!!");
		event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My SSN is 123-45-6789 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("123-45-6789"));
		assertTrue(json.contains("My SSN is !!!! and you shouldn't see it"));

	}

	@Test
	void testNegativeCases() {
		AuditEventMasker masker = new PropertyAuditEventMasker();
		AuditEvent event1 = new GenericAuditEvent("EVENT1");

		String json = masker.maskToJson(event1);
		assertEquals(event1.toJson(), json);
	}

}
