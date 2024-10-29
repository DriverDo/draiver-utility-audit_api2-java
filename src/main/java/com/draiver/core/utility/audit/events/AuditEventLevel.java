package com.draiver.core.utility.audit.events;

public enum AuditEventLevel {
	DEBUG("DEBUG",4),
	INFO("INFO",3),
	WARN("WARN",2),
	ERROR("ERROR",1),
	FATAL("FATAL",0);

	public final String label;
	public final int level;

	private AuditEventLevel(String label, int level) {
		this.label = label;
		this.level = level;
	}

	@Override
	public String toString() {
		return this.label;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	
}
