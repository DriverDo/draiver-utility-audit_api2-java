package com.draiver.core.utility.audit.events;

public interface EventConfig {

	String getAppName();

	String getConversationId();

	String getDivision();

	String getEnv();

	String getExperienceId();

	String getMachine();

	String getModuleName();

	String getNamespace();
	
	String getRegion();
	
	String getTenantId();
	
	String getPartnerId();

	String getSequenceId();

	String getSessionId();

	String getTransactionId();

	void setAppName(String value);

	void setConversationId(String value);

	void setDivision(String value);

	void setEnv(String value);

	void setExperienceId(String value);

	void setMachine(String value);

	void setModuleName(String value);

	void setNamespace(String value);
	
	void setRegion(String value);
	
	void setTenantId(String value);
	
	void setPartnerId(String value);

	void setSequenceId(String value);

	void setSessionId(String value);

	void setTransactionId(String value);
	
}
