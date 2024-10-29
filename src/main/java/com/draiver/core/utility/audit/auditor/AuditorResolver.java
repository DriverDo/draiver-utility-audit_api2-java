package com.draiver.core.utility.audit.auditor;

public interface AuditorResolver {

	Auditor createAuditor();

	Auditor createAuditor(Class<?> declaringType);

}
