package com.draiver.core.utility.audit.masker;

import java.util.List;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.filter.AuditEventFilter;

public interface AuditEventMasker {

    List<AuditEventFilter> getFilters();

    String maskToJson(AuditEvent auditEvent);

    String maskToJson(AuditEvent auditEvent, String json);

    boolean shouldMask(AuditEvent auditEvent);

    

}
