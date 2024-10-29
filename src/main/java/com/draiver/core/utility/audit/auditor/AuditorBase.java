package com.draiver.core.utility.audit.auditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.draiver.core.utility.audit.appender.AuditAppender;
import com.draiver.core.utility.audit.configuration.AuditorConfigurationManager;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.ExceptionAuditEvent;
import com.draiver.core.utility.audit.exception.AuditException;

public abstract class AuditorBase implements Auditor {

	private static final Logger logger = LoggerFactory.getLogger(AuditorBase.class);

	protected boolean insertEventSource = true;
	protected String eventSource = "";
	protected List<AuditAppender> appenders = new ArrayList<>();

	protected EventConfig eventConfig;

	public AuditorBase() {
		this.insertEventSource = true;
	}

	public AuditorBase(String eventSource) {
		this();
		this.eventSource = eventSource;
	}

	public AuditorBase(AuditAppender auditAppender) {
		this("", auditAppender);
	}

	public AuditorBase(String eventSource, AuditAppender auditAppender) {
		this(eventSource);
		this.appenders.add(auditAppender);
	}

	public AuditorBase(List<AuditAppender> auditAppenders) {
		this("", auditAppenders);
	}

	public AuditorBase(String eventSource, List<AuditAppender> auditAppenders) {
		this(eventSource);
		this.appenders = auditAppenders;
	}

	protected void audit(AuditEvent auditEvent) {
		audit(auditEvent, null);
	}

	protected void audit(AuditEvent auditEvent, EventConfig eventConfig) {
		if (this.appenders.isEmpty() || auditEvent == null) {
			return;
		}

		// if auditor level event config is set, update event
		if (this.eventConfig != null) {
			AuditEventUtils.populateAuditEvent(this.eventConfig, auditEvent, false);
		}

		// now check to see if an override was given
		if (eventConfig != null) {
			AuditEventUtils.populateAuditEvent(eventConfig, auditEvent, false);
		}

		if (this.insertEventSource && (auditEvent.getEventSource() == null || auditEvent.getEventSource().isEmpty())) {
			if (this.eventSource == null || this.eventSource.isEmpty()) {
				auditEvent.setEventSource(AuditEventUtils.fetchCurrentEventSource(0));
			} else {
				auditEvent.setEventSource(this.eventSource);
			}
		}

		this.appenders.parallelStream().forEach(x -> {
			try {
				x.append(auditEvent);
			} catch (AuditException e) {
				logger.error("Unable to submit audit", e);
			}
		});
	}

	protected void audit(AuditEvent[] auditEvents) {
		audit(auditEvents, null);
	}

	protected void audit(AuditEvent[] auditEvents, EventConfig eventConfig) {
		audit(Arrays.asList(auditEvents), eventConfig);
	}

	protected void audit(Collection<AuditEvent> auditEvents) {
		audit(auditEvents, null);
	}

	protected void audit(Collection<AuditEvent> auditEvents, EventConfig eventConfig) {
		if (auditEvents == null) {
			return;
		}

		if (this.insertEventSource && this.eventSource.isEmpty()) {
			this.eventSource = AuditEventUtils.fetchCurrentEventSource();
		}

		auditEvents.parallelStream().forEach(x -> audit(x, eventConfig));
	}

	@Override
	public void awaitAyncAudits(List<CompletableFuture<Void>> futures) {
		try {
			CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
		} catch (Exception e) {
			logger.error("Unable to submit audit", e);
			// eat on purpose
		}
	}

	@Override
	public void configure(AuditorConfigurationManager configurationManager) {
		configurationManager.configure(this);
	}

	@Override
	public List<AuditAppender> getAppenders() {
		return this.appenders;
	}

	@Override
	public boolean getInsertEventSource() {
		return this.insertEventSource;
	}

	@Override
	public EventConfig getEventConfig() {
		return this.eventConfig;
	}

	@Override
	public void setEventConfig(EventConfig value) {
		this.eventConfig = value;
	}

	@Override
	public void setInsertEventSource(boolean value) {
		this.insertEventSource = value;
	}

	@Override
	public void throwException(Exception ex) {
		audit(new ExceptionAuditEvent(ex));
	}

	@Override
	public CompletableFuture<Void> throwExceptionAsync(Exception ex) {
		return auditAsync(new ExceptionAuditEvent(ex));
	}

}
