package test.com.draiver.core.utility.audit.masker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.masker.SensitiveDataAuditEventMasker;

class SensitiveDataAuditEventMaskerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSensitiveDataAuditEventMasker() {
		SensitiveDataAuditEventMasker masker = new SensitiveDataAuditEventMasker("****");

		String json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertTrue(json.contains("EVENT1"));
		assertNotNull(AuditEventUtils.fromJson(json));

		AuditEvent event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My SSN is 123-45-6789 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("123-45-6789"));
		assertTrue(json.contains("My SSN is **** and you shouldn't see it"));
		
		event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My credit card is 4803725615987404 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("4803725615987404"));
		assertTrue(json.contains("My credit card is **** and you shouldn't see it"));
		
		event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My credit card is 4803-7256-1598-7404 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("4803-7256-1598-7404"));
		assertTrue(json.contains("My credit card is **** and you shouldn't see it"));
		
		event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My SSN is 123-45-6789 and my credit card is 4803-7256-1598-7404 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("4803-7256-1598-7404"));
		assertFalse(json.contains("123-45-6789"));
		assertTrue(json.contains("My SSN is **** and my credit card is **** and you shouldn't see it"));
	}

	@Test
	void testSSN() {
		SensitiveDataAuditEventMasker masker = new SensitiveDataAuditEventMasker("****");

		String json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertTrue(json.contains("EVENT1"));
		assertNotNull(AuditEventUtils.fromJson(json));

		AuditEvent event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My SSN is 123-45-6789 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("123-45-6789"));
		assertTrue(json.contains("My SSN is **** and you shouldn't see it"));
	}
	
	@Test
	void testCreditCard() {
		SensitiveDataAuditEventMasker masker = new SensitiveDataAuditEventMasker("****");

		String json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertTrue(json.contains("EVENT1"));
		assertNotNull(AuditEventUtils.fromJson(json));

		GenericAuditEvent event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My credit card is 4803725615987404 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("4803725615987404"));
		assertTrue(json.contains("My credit card is **** and you shouldn't see it"));
		
		event1 = new GenericAuditEvent("EVENT1");
		event1.setMessage("My credit card is 4803-7256-1598-7404 and you shouldn't see it");
		json = masker.maskToJson(event1);
		assertFalse(json.contains("4803-7256-1598-7404"));
		assertTrue(json.contains("My credit card is **** and you shouldn't see it"));
	}
	
}
