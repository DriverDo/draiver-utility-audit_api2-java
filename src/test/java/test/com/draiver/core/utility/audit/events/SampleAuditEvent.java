package test.com.draiver.core.utility.audit.events;

import com.draiver.core.utility.audit.events.AuditEventBase;
import com.draiver.core.utility.audit.events.EventConfig;

public class SampleAuditEvent extends AuditEventBase {

	private static final long serialVersionUID = -2689516445620693730L;

	private static final String KEY_PARAMETER1 = "param1";
	private static final String KEY_PARAMETER2 = "param2";
	private static final String KEY_PARAMETER3 = "param3";
	private static final String KEY_PARAMETER4 = "param4";

	public SampleAuditEvent() {
		this(null);
	}

	public SampleAuditEvent(EventConfig eventConfig) {
		super(eventConfig, null);
		safeUpdateInternalParameter(EVENT_TYPE, "SampleAuditEvent");
		safeUpdateInternalParameter(EVENT_NAME, "SampleAuditEvent");
		safeAddExtraParameter(KEY_PARAMETER1, "");
		safeAddExtraParameter(KEY_PARAMETER2, "");
		safeAddExtraTypedParameter(KEY_PARAMETER3, (float) 0);
		safeAddExtraTypedParameter(KEY_PARAMETER4, false);
	}

	public SampleAuditEvent(String parameter1, String parameter2) {
		this(parameter1, parameter2, null);
	}

	public SampleAuditEvent(String parameter1, String parameter2, EventConfig eventConfig) {
		this(eventConfig);
		safeAddExtraParameter(KEY_PARAMETER1, parameter1);
		safeAddExtraParameter(KEY_PARAMETER2, parameter2);
	}

	public String getParameter1() {
		return safeGetExtraParameter(KEY_PARAMETER1);
	}

	public String getParameter2() {
		return safeGetExtraParameter(KEY_PARAMETER2);
	}

	public float getParameter3() {
		return safeGetExtraTypedParameter(KEY_PARAMETER3, (float) 0);
	}

	public boolean getParameter4() {
		return safeGetExtraTypedParameter(KEY_PARAMETER4, false);
	}

	public void setParameter1(String value) {
		safeAddExtraParameter(KEY_PARAMETER1, value);
	}

	public void setParameter2(String value) {
		safeAddExtraParameter(KEY_PARAMETER2, value);
	}

	public void setParameter3(float value) {
		safeAddExtraTypedParameter(KEY_PARAMETER3, value);
	}

	public void setParameter4(boolean value) {
		safeAddExtraTypedParameter(KEY_PARAMETER4, value);
	}

}
