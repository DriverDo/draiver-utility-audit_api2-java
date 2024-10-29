package com.draiver.core.utility.audit.events;

public enum AuditEventStatus {

	TRANSACTION_BEGIN("TRANSACTION_BEGIN"),
	TRANSACTION_INPROGRESS("TRANSACTION_INPROGRESS"),
	TRANSACTION_END_SUCCESS("TRANSACTION_END_SUCCESS"),
	TRANSACTION_END_PARTIAL_SUCCESS("TRANSACTION_END_PARTIAL_SUCCESS"),
	TRANSACTION_END_FAIL("TRANSACTION_END_FAIL"),
	SUCCESS("SUCCESS"),
	PARTIAL_SUCCESS("PARTIAL_SUCCESS"),
	FAIL("FAIL");
	
	public final String label;
	
	private AuditEventStatus(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return this.label;
	}	
}
