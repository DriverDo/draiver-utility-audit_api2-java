package com.draiver.core.utility.audit.masker;

import java.util.ArrayList;
import java.util.List;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.filter.AuditEventFilter;
import com.draiver.core.utility.audit.filter.AuditEventFilterHelper;

public abstract class JsonOnlyAuditEventMaskerBase implements AuditEventMasker {

	private List<AuditEventFilter> filters = new ArrayList<>();

	protected abstract String onMaskToJson(String json);

	public JsonOnlyAuditEventMaskerBase() {
	}

	@Override
	public List<AuditEventFilter> getFilters() {
		return this.filters;
	}

	@Override
	public String maskToJson(AuditEvent auditEvent) {
		if (shouldMask(auditEvent)) {
			return maskToJson(auditEvent, auditEvent.toJson());
		}
		return auditEvent.toJson();
	}

	@Override
	public String maskToJson(AuditEvent auditEvent, String json) {
		if (shouldMask(auditEvent)) {
			return onMaskToJson(json);
		}
		return json;
	}

	@Override
	public boolean shouldMask(AuditEvent auditEvent) {
		return AuditEventFilterHelper.isMatch(this.filters, auditEvent);
	}

}
