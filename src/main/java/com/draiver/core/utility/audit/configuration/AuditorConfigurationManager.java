package com.draiver.core.utility.audit.configuration;

import com.draiver.core.utility.audit.auditor.Auditor;

public interface AuditorConfigurationManager {

	void configure(Auditor auditor);
	
	Auditor createAuditor(Class<?> declaringType);
	
}
