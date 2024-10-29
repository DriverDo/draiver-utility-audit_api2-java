package com.draiver.core.utility.audit.appender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.exception.AuditException;
import com.draiver.core.utility.audit.filter.AuditEventFilter;
import com.draiver.core.utility.audit.filter.AuditEventFilterHelper;
import com.draiver.core.utility.audit.masker.AuditEventMasker;

public abstract class AuditAppenderBase implements AuditAppender {

	private AuditEventLevel minLevel = AuditEventLevel.DEBUG;
	private List<AuditEventFilter> filters = new ArrayList<>();
	private List<AuditEventMasker> maskers = new ArrayList<>();

	protected abstract void onAppend(AuditEvent auditEvent, String maskedJson);

	public AuditAppenderBase() {
		this.minLevel = AuditEventLevel.DEBUG;
		this.filters = new ArrayList<>();
		this.maskers = new ArrayList<>();
	}

	@Override
	public void append(AuditEvent auditEvent){
		if (auditEvent.getEventLevel().getLevel() > this.minLevel.getLevel()) {
			return;
		}

		if (!AuditEventFilterHelper.isMatch(this.filters, auditEvent)) {
			return;
		}

		onAppend(auditEvent, maskToJson(auditEvent));
	}

	@Override
	public CompletableFuture<Void> appendAsync(AuditEvent auditEvent) {
		return appendAsync(auditEvent, ForkJoinPool.commonPool());
	}

	@Override
	public CompletableFuture<Void> appendAsync(AuditEvent auditEvent, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			try {				
				append(auditEvent);
			} catch (AuditException e) {
				return null;
			}
			return null;
		}, executor);
	}

	@Override
	public List<AuditEventFilter> getFilters() {
		return this.filters;
	}

	@Override
	public List<AuditEventMasker> getMaskers() {
		return this.maskers;
	}

	@Override
	public AuditEventLevel getMinLevel() {
		return this.minLevel;
	}

	@Override
	public void setFilters(List<AuditEventFilter> value) {
		this.filters = value;
	}

	@Override
	public void setMaskers(List<AuditEventMasker> value) {
		this.maskers = value;
	}

	@Override
	public void setMinLevel(AuditEventLevel value) {
		this.minLevel = value;
	}

	protected String maskToJson(AuditEvent auditEvent) {
		return maskToJson(auditEvent, auditEvent.toJson());
	}

	protected String maskToJson(AuditEvent auditEvent, String json) {
		if (this.maskers == null || this.maskers.isEmpty()) {
			return json;
		}

		for (AuditEventMasker masker : this.maskers) {
			json = masker.maskToJson(auditEvent, json);
		}

		return json;
	}

}
