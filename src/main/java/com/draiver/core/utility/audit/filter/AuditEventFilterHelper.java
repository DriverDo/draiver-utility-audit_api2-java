package com.draiver.core.utility.audit.filter;

import java.util.List;

import com.draiver.core.utility.audit.events.AuditEvent;

public class AuditEventFilterHelper {

	private AuditEventFilterHelper() {
	}

	public static boolean isMatch(List<AuditEventFilter> filters, AuditEvent auditEvent){
		boolean shouldAllow = true;
		if (filters != null && !filters.isEmpty()) {
			for (AuditEventFilter filter : filters) {
				boolean isMatch = filter.isMatch(auditEvent);
				if (filter.isAllow()) {
					shouldAllow = shouldAllow && isMatch;
				} else {
					if (isMatch) {
						shouldAllow = false;
					}
				}
			}
		}

		return shouldAllow;
	}

}
