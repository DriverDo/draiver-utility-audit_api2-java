package com.draiver.core.utility.audit.appender;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.exception.AuditException;

public class RollingFileAuditAppender extends FileAuditAppender {

	protected static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
	private static final int DEFAULT_MAX_FILESIZE = 52428800;
	private static final int MIN_FILESIZE = 1024;

	private int maxFileSize = DEFAULT_MAX_FILESIZE;
	private String baseFileName = null;

	public RollingFileAuditAppender() {
		this(null, DEFAULT_MAX_FILESIZE);
	}

	public RollingFileAuditAppender(String baseFileName, int maxFileSize) {
		super("");
		this.baseFileName = baseFileName;
		this.maxFileSize = maxFileSize;

		if (this.maxFileSize < MIN_FILESIZE) {
			this.maxFileSize = MIN_FILESIZE;
		}
	}

	public String getBaseFileName() {
		return this.baseFileName;
	}

	public int getMaxFileSize() {
		return this.maxFileSize;
	}

	public void setBaseFileName(String value) {
		this.baseFileName = value;
	}

	public void setMaxFileSize(int value) {
		if (value < MIN_FILESIZE) {
			value = MIN_FILESIZE;
		}
		this.maxFileSize = value;
	}

	@Override
	protected void onAppend(AuditEvent auditEvent, String maskedJson){
		if (this.baseFileName == null || this.baseFileName.isEmpty()) {
			throw new AuditException("baseFileName must be supplied");
		}

		if (this.fileName.isEmpty()) {
			this.fileName = getNextFileName();
		}

		if ((getCurrentFileSize() + maskedJson.getBytes().length) >= this.maxFileSize) {
			this.fileName = getNextFileName();
		}

		super.onAppend(auditEvent, maskedJson);
	}

	private long getCurrentFileSize() {
		try {
			return this.getFile().length();
		} catch (Exception e) {
			return 0;
		}
	}

	private String getNextFileName() {
		File baseFile = new File(this.baseFileName);
		String fileFormat = "%1$s/%2$s-%3$s%4$s";
		String path = baseFile.getParent();
		String fileName = baseFile.getName().replaceFirst("[.][^.]+$", "");
		String fileExtension = baseFile.getName().replaceFirst(fileName, "");
		return String.format(fileFormat, path, fileName, ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)), fileExtension);
	}

}
