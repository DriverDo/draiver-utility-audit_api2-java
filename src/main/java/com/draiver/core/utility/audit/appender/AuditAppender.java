package com.draiver.core.utility.audit.appender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.filter.AuditEventFilter;
import com.draiver.core.utility.audit.masker.AuditEventMasker;

public interface AuditAppender {

    void append(AuditEvent auditEvent);

    CompletableFuture<Void> appendAsync(AuditEvent auditEvent) ;
    
    CompletableFuture<Void> appendAsync(AuditEvent auditEvent, Executor executor) ;

    List<AuditEventFilter> getFilters();

    List<AuditEventMasker> getMaskers();

    AuditEventLevel getMinLevel();

    void setFilters(List<AuditEventFilter> value);

    void setMaskers(List<AuditEventMasker> value);

    void setMinLevel(AuditEventLevel value);
	
}
