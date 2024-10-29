package com.draiver.core.utility.audit.filter;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.draiver.core.utility.audit.events.AuditEvent;

public class DenyAuditEventFilter extends AllowAuditEventFilter {

	public DenyAuditEventFilter() {
	}

	public DenyAuditEventFilter(String pattern) {
		super(null, pattern);
	}

	public DenyAuditEventFilter(String property, String pattern) {
		super(property, pattern);
	}

	@Override
	public boolean isAllow() {
		return false;
	}

	@Override
	public boolean isMatch(AuditEvent auditEvent) {
		try {

			String value = "";
			if (StringUtils.isEmpty(getProperty())) {
				value = auditEvent.toJson();
			} else {
				if (!auditEvent.getParameters().containsKey(getProperty())) {
					return false;
				}
				value = auditEvent.getParameters().get(getProperty());
			}

			Pattern regex = Pattern.compile(getPattern(), Pattern.CASE_INSENSITIVE);
			return regex.matcher(value).find();

		} catch (Exception e) {
			return true;
		}
	}
}