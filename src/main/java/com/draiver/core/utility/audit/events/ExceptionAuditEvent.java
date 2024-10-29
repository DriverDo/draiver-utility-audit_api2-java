package com.draiver.core.utility.audit.events;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import com.draiver.core.exception.DraiverWarningException;

public class ExceptionAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = 2964067570383446724L;

	private static final String TYPE_NAME = "ExceptionAuditEvent";
	private static final String EXCEPTION = "Exception";
	private static final String STACKTRACE = "StackTrace";
	private static final String EXCEPTION_SOURCE = "ExceptionSource";

	public ExceptionAuditEvent(Exception exception) {
		this(EXCEPTION, exception);
	}

	public ExceptionAuditEvent(Exception exception, EventConfig eventConfig) {
		this(EXCEPTION, TYPE_NAME, exception, eventConfig);
		setEventSource(exception);
	}

	public ExceptionAuditEvent(String exceptionName, Exception exception) {
		this(exceptionName, TYPE_NAME, exception);
		setEventSource(exception);
	}

	public ExceptionAuditEvent(String exceptionName, String eventType, Exception exception) {
		this(exceptionName, eventType, exception, null);
	}

	public ExceptionAuditEvent(String exceptionName, String eventType, Exception exception, EventConfig eventConfig) {
		super(exceptionName, eventType, eventConfig, AuditEventLevel.ERROR, AuditEventStatus.FAIL);
		setEventSource(exception);
		setEventCategory("ERROR");

		StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));

		safeAddExtraParameter(EXCEPTION, sw.toString());
		safeAddExtraParameter(STACKTRACE, Arrays.toString(exception.getStackTrace()));
		safeAddExtraParameter(EXCEPTION_SOURCE, getSource(exception));

		setMessage(exception.toString());

		if (exception instanceof DraiverWarningException) {
			setEventName("WARNING");
			setEventLevel(AuditEventLevel.WARN);
			setEventStatus(AuditEventStatus.PARTIAL_SUCCESS);
			setEventCategory("WARNING");
			setEventType("WarningExceptionAuditEvent");
		}
	}

	public String getException() {
		return safeGetExtraParameter(EXCEPTION);
	}

	public String getStackTrace() {
		return safeGetExtraParameter(STACKTRACE);
	}

	public String getExceptionSource() {
		return safeGetExtraParameter(EXCEPTION_SOURCE);
	}

	private static String getSource(Exception e) {
		try {
			List<StackTraceElement> stackList = Arrays.asList(e.getStackTrace());
			if (stackList.isEmpty()) {
				return "<Empty Stack Trace>";
			}
			return stackList.get(0).toString();
		} catch (Exception ex) {
			return "<Empty Stack Trace>";
		}
	}

	private void setEventSource(Exception e) {
		setEventSource(getSource(e));
	}
}
