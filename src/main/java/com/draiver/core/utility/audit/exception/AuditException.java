package com.draiver.core.utility.audit.exception;

public class AuditException extends RuntimeException {

	private static final long serialVersionUID = 5873546960869633823L;

	public AuditException() {
	}

	public AuditException(String message) {
		super(message);
	}

	public AuditException(Throwable throwable) {
		super(throwable);
	}

	public AuditException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public AuditException(String message, Throwable throwable, boolean arg2, boolean arg3) {
		super(message, throwable, arg2, arg3);
	}

}
