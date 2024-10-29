package test.com.draiver.core.utility.audit.auditor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.appender.InMemoryAuditAppender;
import com.draiver.core.utility.audit.appender.NoOpAuditAppender;
import com.draiver.core.utility.audit.auditor.Auditor;
import com.draiver.core.utility.audit.auditor.AuditorFactory;
import com.draiver.core.utility.audit.auditor.ConfigurationAuditorResolver;
import com.draiver.core.utility.audit.auditor.StandardAuditor;
import com.draiver.core.utility.audit.configuration.XmlAuditorConfigurationManager;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.GenericAuditEvent;

class AuditorFactoryTest {

	@Test
	void testCreateAuditorNoParams() {
		Auditor auditor  = AuditorFactory.createAuditor();
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof NoOpAuditAppender);
	}
	
	@Test
	void testCreateAuditorFilePath() {
		Auditor auditor  = AuditorFactory.createAuditor("/auditor-test001.xml");
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
	}
	
	@Test
	void testCreateAuditorFilePathWithSpaces() {
		Auditor auditor  = AuditorFactory.createAuditor("/auditor with spaces.xml");
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
	}
	
	@Test
	void testCreateAuditorFile() {
		Auditor auditor  = AuditorFactory.createAuditor(loadFile("auditor-test001.xml"));
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
	}
	
	@Test
	void testCreateAuditorDeclaringType() {
		Auditor auditor  = AuditorFactory.createAuditor(this.getClass());
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof NoOpAuditAppender);
	}
	
	@Test
	void testCreateAuditorDeclaringTypeFilePath() {
		Auditor auditor  = AuditorFactory.createAuditor(this.getClass(), "/auditor-test001.xml");
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
	}
	
	@Test
	void testCreateAuditorDeclaringTypeFile() {
		Auditor auditor  = AuditorFactory.createAuditor(this.getClass(), loadFile("auditor-test001.xml"));
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
	}
	
	@Test
	void testCreateAuditorDeclaringTypeResolver() {		
		Auditor auditor  = AuditorFactory.createAuditor(this.getClass(), new ConfigurationAuditorResolver(new XmlAuditorConfigurationManager("/auditor-test001.xml")));
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
	}
	
	@Test
	void testAppender() throws InterruptedException {
		Auditor auditor  = AuditorFactory.createAuditor("/auditor-test001.xml");
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1,auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
		
		for(int i=0; i<10; i++) {
			AuditEvent tmpEvent = new GenericAuditEvent("EVENT" + i);
			auditor.auditAsync(tmpEvent);
		}
		
		Thread.sleep(1000);
		
		InMemoryAuditAppender inMemoryAuditor = ((InMemoryAuditAppender)auditor.getAppenders().get(0));
		List<String> messages = inMemoryAuditor.getMessages();
		
		assertEquals(100,inMemoryAuditor.getMaxMessages());
		assertEquals(10,messages.size());
		assertNotNull(messages.stream().filter(x-> x.contains(this.getClass().getSimpleName())).findFirst().get());		
	}
	
	private static File loadFile(String path) {
		if (path == null || path.isEmpty()) {
			return null;
		}

		File tmpFile = new File(path);
		if (tmpFile.exists()) {
			return tmpFile;
		}

		try {
			tmpFile = new File(new URI(AuditorFactoryTest.class.getClassLoader().getResource(path).getFile()).getPath());
			if (tmpFile.exists()) {
				return tmpFile;
			}
		} catch (Exception e) {

		}
		return null;
	}

}
