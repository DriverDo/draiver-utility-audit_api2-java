package com.draiver.core.utility.audit.masker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExAuditEventMasker extends JsonOnlyAuditEventMaskerBase {

	private static final String DEFAULT_REPLACEWITH = "****";
	private static final String DEFAULT_REGEXPATTERN = ".*";

	private String replaceWith = DEFAULT_REPLACEWITH;
	private String regExPattern = DEFAULT_REGEXPATTERN;

	public RegExAuditEventMasker() {
		this(DEFAULT_REGEXPATTERN, DEFAULT_REPLACEWITH);
	}

	public RegExAuditEventMasker(String regExPattern, String replaceWith) {
		this.regExPattern = regExPattern;
		this.replaceWith = replaceWith;
	}

	public String getRegExPattern() {
		return this.regExPattern;
	}

	public String getReplaceWith() {
		return this.replaceWith;
	}

	@Override
	protected String onMaskToJson(String json) {

		if (this.regExPattern.isEmpty()) {
			this.regExPattern = "";
		}

		if (this.replaceWith.isEmpty()) {
			this.replaceWith = "";
		}

		if (this.regExPattern.isEmpty() || this.replaceWith.isEmpty()) {
			return json;
		}

		Pattern regex1 = Pattern.compile(this.regExPattern);
		Matcher matcher1 = regex1.matcher(json);
		return matcher1.replaceFirst(this.replaceWith);
	}

	public void setRegExPattern(String value) {
		this.regExPattern = value;
	}

	public void setReplaceWith(String value) {
		this.replaceWith = value;
	}

}
