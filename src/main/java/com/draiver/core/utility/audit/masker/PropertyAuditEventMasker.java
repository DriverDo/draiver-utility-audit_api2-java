package com.draiver.core.utility.audit.masker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyAuditEventMasker extends JsonOnlyAuditEventMaskerBase {

	private static final String DEFAULT_REPLACEWITH = "****";
	private static final String DEFAULT_REGEXPATTERN = ".*";

	private String propertyName = "";
	private String replaceWith = DEFAULT_REPLACEWITH;
	private String regExPattern = DEFAULT_REGEXPATTERN;

	public PropertyAuditEventMasker() {
		this("", DEFAULT_REGEXPATTERN, DEFAULT_REPLACEWITH);
	}

	public PropertyAuditEventMasker(String propertyName, String replaceWith) {
		this(propertyName, DEFAULT_REGEXPATTERN, replaceWith);
	}

	public PropertyAuditEventMasker(String propertyName, String regExPattern, String replaceWith) {
		this.propertyName = propertyName;
		this.regExPattern = regExPattern;
		this.replaceWith = replaceWith;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public String getRegExPattern() {
		return this.regExPattern;
	}

	public String getReplaceWith() {
		return this.replaceWith;
	}

	@Override
	protected String onMaskToJson(String json) {
		if (this.propertyName.isEmpty()) {
			this.propertyName = "";
		}
		if (this.regExPattern.isEmpty()) {
			this.regExPattern = "";
		}
		if (this.replaceWith.isEmpty()) {
			this.replaceWith = "";
		}

		String patternFormat = "(\"%1$s\":\")(.*?)(\")";
		Pattern regex1 = Pattern.compile(String.format(patternFormat, this.propertyName), Pattern.MULTILINE);		
		Matcher matcher1 = regex1.matcher(json);
		if (!matcher1.find()) {
			return json;
		}

		Pattern regex2 = Pattern.compile(this.regExPattern, Pattern.MULTILINE);
		Matcher matcher2 = regex2.matcher(matcher1.group(2));
		
		String replaceText = matcher1.group(1) + matcher2.replaceFirst(this.replaceWith) + matcher1.group(3);
		return matcher1.replaceFirst(replaceText);
	}

	public void setPropertyName(String value) {
		this.propertyName = value;
	}

	public void setRegExPattern(String value) {
		this.regExPattern = value;
	}

	public void setReplaceWith(String value) {
		this.replaceWith = value;
	}

}
