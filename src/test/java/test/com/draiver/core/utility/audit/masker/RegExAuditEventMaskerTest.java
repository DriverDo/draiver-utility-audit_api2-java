package test.com.draiver.core.utility.audit.masker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.masker.RegExAuditEventMasker;

class RegExAuditEventMaskerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testRegExAuditEventMasker() {
		RegExAuditEventMasker masker = new RegExAuditEventMasker("EVENT1","****");
		String json = masker.maskToJson(new GenericAuditEvent("EVENT1"));
		assertFalse(json.contains("EVENT1"));
		assertTrue(json.contains("****"));
	}

}
