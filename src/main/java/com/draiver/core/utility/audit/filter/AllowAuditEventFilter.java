package com.draiver.core.utility.audit.filter;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.draiver.core.utility.audit.events.AuditEvent;

public class AllowAuditEventFilter extends AuditEventFilterBase {

	private String pattern;
	private String property;

	public AllowAuditEventFilter() {
		this(null, null);
	}

	public AllowAuditEventFilter(String pattern) {
		this(null, pattern);
	}

	public AllowAuditEventFilter(String property, String pattern) {
		this.pattern = pattern;
		this.property = property;
	}

	@Override
	public boolean isAllow() {
		return true;
	}

	@Override
	public boolean isMatch(AuditEvent auditEvent) {
		try {

			String value = "";
			if (StringUtils.isEmpty(this.property)) {
				value = auditEvent.toJson();
			} else {
				if (!auditEvent.getParameters().containsKey(this.property)) {
					return true;
				}
				value = auditEvent.getParameters().get(this.property);
			}

			Pattern regex = Pattern.compile(this.pattern, Pattern.CASE_INSENSITIVE);
			return regex.matcher(value).find();

		} catch (Exception e) {
			return true;
		}
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String value) {
		pattern = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String value) {
		property = value;
	}

}