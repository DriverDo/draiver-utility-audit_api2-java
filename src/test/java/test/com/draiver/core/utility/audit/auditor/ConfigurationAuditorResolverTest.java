package test.com.draiver.core.utility.audit.auditor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.appender.InMemoryAuditAppender;
import com.draiver.core.utility.audit.appender.NoOpAuditAppender;
import com.draiver.core.utility.audit.auditor.Auditor;
import com.draiver.core.utility.audit.auditor.ConfigurationAuditorResolver;
import com.draiver.core.utility.audit.auditor.StandardAuditor;
import com.draiver.core.utility.audit.configuration.XmlAuditorConfigurationManager;

class ConfigurationAuditorResolverTest {

	@Test
	void testCreateAuditor() {
		ConfigurationAuditorResolver resolver = new ConfigurationAuditorResolver();
		Auditor auditor = resolver.createAuditor();
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1, auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof NoOpAuditAppender);
	}

	@Test
	void testCreateAuditorDeclaringType() {
		ConfigurationAuditorResolver resolver = new ConfigurationAuditorResolver();
		Auditor auditor = resolver.createAuditor(this.getClass());
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1, auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof NoOpAuditAppender);
	}
	
	@Test
	void testCreateAuditorConfigurationManager() {
		ConfigurationAuditorResolver resolver = new ConfigurationAuditorResolver(new XmlAuditorConfigurationManager());
		Auditor auditor = resolver.createAuditor();
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1, auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof NoOpAuditAppender);
	}
	
	@Test
	void testCreateAuditorConfigurationManagerWithFile() {
		ConfigurationAuditorResolver resolver = new ConfigurationAuditorResolver(new XmlAuditorConfigurationManager("/auditor-test001.xml"));
		Auditor auditor = resolver.createAuditor();
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1, auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof InMemoryAuditAppender);
		
		InMemoryAuditAppender inMemoryAuditAppender = (InMemoryAuditAppender)auditor.getAppenders().get(0);
		assertEquals(100,inMemoryAuditAppender.getMaxMessages());
	}
	
	@Test
	void testCreateAuditorConfigurationManagerWithBadFile() {
		ConfigurationAuditorResolver resolver = new ConfigurationAuditorResolver(new XmlAuditorConfigurationManager("/bad-auditor-test001.xml"));
		Auditor auditor = resolver.createAuditor();
		assertNotNull(auditor);
		assertTrue(auditor instanceof StandardAuditor);
		assertEquals(1, auditor.getAppenders().size());
		assertTrue(auditor.getAppenders().get(0) instanceof NoOpAuditAppender);		
	}

}
