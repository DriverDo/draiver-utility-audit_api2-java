package com.draiver.core.utility.audit.events;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StopwatchAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = 2012613900703611375L;

	private static final String TYPE_NAME = "StopwatchAuditEvent";
	private static final String EVENTNAME = "Stopwatch";

	private static final String PARAM_STARTTIME = "StartTime";
	private static final String PARAM_STOPTIME = "StopTime";
	private static final String PARAM_DURATION = "DurationMs";

	private static final String DATETIME_FORMAT_MILI = "yyyyMMddHHmmss.SSSXXX";

	public StopwatchAuditEvent() {
		this(ZonedDateTime.now(), null);
	}

	public StopwatchAuditEvent(EventConfig eventConfig) {
		this(ZonedDateTime.now(), eventConfig);
	}

	public StopwatchAuditEvent(ZonedDateTime startTime) {
		this(startTime, null);
	}

	public StopwatchAuditEvent(ZonedDateTime startTime, EventConfig eventConfig) {
		super(EVENTNAME, TYPE_NAME, eventConfig, AuditEventLevel.DEBUG, AuditEventStatus.SUCCESS);

		setEventCategory("TIMING");

		if (startTime == null) {
			startTime = ZonedDateTime.now();
		}

		setStartTime(startTime);
		setStopTime(startTime);

		safeAddExtraTypedParameter(PARAM_DURATION, (long)0);
	}

	public long getDurationMs() {
		if (getStartTime().equals(getStopTime())) {
			return Duration.between(getStartTime(), ZonedDateTime.now()).toMillis();
		}
		return safeGetExtraTypedParameter(PARAM_DURATION,(long)0);
	}

	public ZonedDateTime getStartTime() {
		return ZonedDateTime.parse(safeGetExtraParameter(PARAM_STARTTIME), DateTimeFormatter.ofPattern(DATETIME_FORMAT_MILI));
	}

	public ZonedDateTime getStopTime() {
		return ZonedDateTime.parse(safeGetExtraParameter(PARAM_STOPTIME), DateTimeFormatter.ofPattern(DATETIME_FORMAT_MILI));
	}

	public void setDurationMs(long value) {
		safeAddExtraTypedParameter(PARAM_DURATION, value);
		updateMessage();
	}

	public void setStartTime(ZonedDateTime value) {
		safeAddExtraParameter(PARAM_STARTTIME, value.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_MILI)));
		updateMessage();
	}

	public void setStopTime(ZonedDateTime value) {
		safeAddExtraParameter(PARAM_STOPTIME, value.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_MILI)));
		updateDuration();
		updateMessage();
	}

	private void updateDuration() {
		safeAddExtraTypedParameter(PARAM_DURATION, Duration.between(getStartTime(), getStopTime()).toMillis());
		updateMessage();
	}

	private void updateMessage() {
		String durationMs = "";
		String startTime = "";
		String endTime = "";

		try {
			durationMs = Long.toString(getDurationMs());
		} catch (Exception ex) {
			durationMs = "";
		}

		try {
			startTime = getStartTime().toString();
		} catch (Exception ex) {
			startTime = "";
		}

		try {
			endTime = getStopTime().toString();
		} catch (Exception ex) {
			endTime = "";
		}

		setMessage(String.format("Source: %s, Duration: %s ms. Start Time: %s, Stop Time: %s", getEventSource(), durationMs, startTime, endTime));
	}

	public void start() {
		setStartTime(ZonedDateTime.now());
		setStopTime(ZonedDateTime.now());
	}

	public void stop() {
		setStopTime(ZonedDateTime.now());
		updateDuration();
	}

	@Override
	public String toJson(boolean prettyPrint) {
		updateDuration();
		return super.toJson(prettyPrint);
	}

}
