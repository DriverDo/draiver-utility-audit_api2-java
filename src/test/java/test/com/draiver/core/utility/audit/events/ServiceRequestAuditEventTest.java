package test.com.draiver.core.utility.audit.events;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.events.ServiceRequestAuditEvent;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceRequestAuditEventTest {

	@Test
	public void testGetterSetter() {
		Map<String, Object> samplePayload = new HashMap<>();
		samplePayload.put("key1", "value1");
		samplePayload.put("key2", "value2");
		samplePayload.put("key3", "value3");

		ServiceRequestAuditEvent event1 = new ServiceRequestAuditEvent("SERVICE1");
		event1.setServicePayload(samplePayload);
		event1.setServiceVersion("1.0");

		assertEquals("SERVICE1", event1.getServiceName());
		assertEquals("1.0", event1.getServiceVersion());
		assertTrue(event1.getServicePayload().containsKey("key1"));
		assertTrue(event1.toJson().contains("key1"));

	}

	@Test
	public void testGetMessage() {

		ServiceRequestAuditEvent event1 = new ServiceRequestAuditEvent("SERVICE1");
		String myMessage = "message";
		event1.setMessage(myMessage);
		String setMessage = event1.getMessage();
		assertEquals(myMessage, setMessage);

		event1.setServiceName("service");
		setMessage = event1.getMessage();
		assertEquals(myMessage, setMessage);

		event1.setServiceMethod("method");
		setMessage = event1.getMessage();
		assertNotEquals(myMessage, setMessage);
		assertTrue(setMessage.startsWith("service"));
		assertTrue(setMessage.contains("method"));
		assertTrue(setMessage.contains(myMessage));
	}
}
