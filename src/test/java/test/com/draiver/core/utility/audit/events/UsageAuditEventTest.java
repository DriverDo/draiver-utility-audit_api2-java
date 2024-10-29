package test.com.draiver.core.utility.audit.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.UsageAuditEvent;

public class UsageAuditEventTest {

	@Test
	void testGetterSetters() {
		UsageAuditEvent event2 = new UsageAuditEvent();
		event2.setUsageItemProvider("PROVIDER1");
		event2.setUsageItem("search");
		event2.setUsageItemData("MY DATA");
		event2.setUsageAmount(1);
		String json = event2.toJson(false);

		assertEquals("PROVIDER1", event2.getUsageItemProvider());
		assertEquals("search", event2.getUsageItem());
		assertEquals("MY DATA", event2.getUsageItemData());
		assertEquals(1, event2.getUsageAmount());
		assertTrue(json.contains("MY DATA"));
	}

}
