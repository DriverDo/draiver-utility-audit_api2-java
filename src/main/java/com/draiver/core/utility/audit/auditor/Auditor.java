package com.draiver.core.utility.audit.auditor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.draiver.core.utility.audit.appender.AuditAppender;
import com.draiver.core.utility.audit.configuration.AuditorConfigurationManager;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.EventConfig;

public interface Auditor {

	CompletableFuture<Void> auditAsync(AuditEvent auditEvent);

	CompletableFuture<Void> auditAsync(AuditEvent auditEvent, EventConfig eventConfig);

	CompletableFuture<Void> auditAsync(AuditEvent[] auditEvents);

	CompletableFuture<Void> auditAsync(AuditEvent[] auditEvents, EventConfig eventConfig);

	CompletableFuture<Void> auditAsync(Collection<AuditEvent> auditEvents);

	CompletableFuture<Void> auditAsync(Collection<AuditEvent> auditEvents, EventConfig eventConfig);

	void awaitAyncAudits(List<CompletableFuture<Void>> futures);		
	
	void configure(AuditorConfigurationManager configurationManager);	
	
	List<AuditAppender> getAppenders();

	boolean getInsertEventSource();
	
	EventConfig getEventConfig();
	
	void setEventConfig(EventConfig value);

	void setInsertEventSource(boolean value);

	void throwException(Exception ex);

	CompletableFuture<Void> throwExceptionAsync(Exception ex);

}
