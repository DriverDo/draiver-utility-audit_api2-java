package com.draiver.core.utility.audit.masker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensitiveDataAuditEventMasker extends JsonOnlyAuditEventMaskerBase {

	private static final String DEFAULT_REPLACEWITH = "****";

	private String replaceWith = DEFAULT_REPLACEWITH;

	public SensitiveDataAuditEventMasker() {
		this(DEFAULT_REPLACEWITH);
	}

	public SensitiveDataAuditEventMasker(String replaceWith) {
		if (replaceWith == null) {
			this.replaceWith = "";
		} else {
			this.replaceWith = replaceWith;
		}
	}

	@Override
	protected String onMaskToJson(String json) {
		if (this.replaceWith == null) {
			this.replaceWith = "";
		}

		// look for credit card numbers
		Pattern regex = Pattern.compile("\\d{4}-?\\d{4}-?\\d{4}-?\\d{4}", Pattern.MULTILINE);
		Matcher matcher = regex.matcher(json);
		json = matcher.replaceAll(this.replaceWith);

		// look for SSN
		regex = Pattern.compile("\\d{3}-\\d{2}-\\d{4}", Pattern.MULTILINE);
		matcher = regex.matcher(json);
		json = matcher.replaceAll(this.replaceWith);

		return json;
	}

}
