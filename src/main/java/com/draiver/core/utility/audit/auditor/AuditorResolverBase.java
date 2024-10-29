package com.draiver.core.utility.audit.auditor;

import java.util.List;

import com.draiver.core.utility.audit.events.AuditEventUtils;

public abstract class AuditorResolverBase implements AuditorResolver {

	public AuditorResolverBase() {
		// default Constructor
	}

	@Override
	public Auditor createAuditor() {
		try {
			List<StackTraceElement> stackTrace = AuditEventUtils.fetchFilteredStackTrace(AuditEventUtils.getStackTraceElementExcludeList());
			if (stackTrace.isEmpty()) {
				return createAuditor(null);
			}

			return createAuditor(Class.forName(stackTrace.get(0).getClassName(),true,Thread.currentThread().getContextClassLoader()));
		} catch (Exception e) {
			return createAuditor(null);
		}
	}

}
