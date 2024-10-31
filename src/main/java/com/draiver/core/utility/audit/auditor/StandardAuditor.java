package com.draiver.core.utility.audit.auditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.draiver.core.utility.audit.appender.AuditAppender;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.EventConfig;

public class StandardAuditor extends AuditorBase {

	public StandardAuditor() {
		this.insertEventSource = true;
	}

	public StandardAuditor(String eventSource) {
		super(eventSource);
	}

	public StandardAuditor(AuditAppender auditAppender) {
		super(auditAppender);
	}

	public StandardAuditor(String eventSource, AuditAppender auditAppender) {
		super(eventSource, auditAppender);
	}

	public StandardAuditor(List<AuditAppender> auditAppenders) {
		super(auditAppenders);
	}

	public StandardAuditor(String eventSource, List<AuditAppender> auditAppenders) {
		super(eventSource, auditAppenders);
	}

	@Override
	public CompletableFuture<Void> auditAsync(AuditEvent auditEvent) {
		return auditAsync(auditEvent, null);
	}

	@Override
	public CompletableFuture<Void> auditAsync(AuditEvent[] auditEvents) {
		return auditAsync(auditEvents, null);
	}

	@Override
	public CompletableFuture<Void> auditAsync(Collection<AuditEvent> auditEvents) {
		return auditAsync(auditEvents, null);
	}

	@Override
	public CompletableFuture<Void> auditAsync(AuditEvent auditEvent, EventConfig eventConfig) {
		if (this.insertEventSource) {
			if ((auditEvent.getEventSource() == null || auditEvent.getEventSource().isEmpty()) && this.eventSource.isEmpty()) {
				auditEvent.setEventSource(AuditEventUtils.fetchCurrentEventSource());
			} else {
				auditEvent.setEventSource(this.eventSource);
			}
		}
		return CompletableFuture.supplyAsync(() -> {
			audit(auditEvent, eventConfig);
			return null;
		});
	}

	@Override
	public CompletableFuture<Void> auditAsync(AuditEvent auditEvent, EventConfig eventConfig, Executor asyncExecutor) {
		return auditAsync(auditEvent, eventConfig);
	}

	@Override
	public CompletableFuture<Void> auditAsync(AuditEvent[] auditEvents, EventConfig eventConfig) {
		return auditAsync(Arrays.asList(auditEvents), eventConfig);

	}

	@Override
	public CompletableFuture<Void> auditAsync(Collection<AuditEvent> auditEvents, EventConfig eventConfig) {
		if (auditEvents == null) {
			return CompletableFuture.supplyAsync(() -> null);
		}

		List<CompletableFuture<?>> futures = new ArrayList<>();
		for (AuditEvent auditEvent : auditEvents) {
			futures.add(auditAsync(auditEvent, eventConfig));
		}

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
	}

}
