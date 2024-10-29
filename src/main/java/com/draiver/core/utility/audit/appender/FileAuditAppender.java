package com.draiver.core.utility.audit.appender;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.exception.AuditException;

public class FileAuditAppender extends AuditAppenderBase {

	protected String fileName = null;

	public FileAuditAppender() {
		this((String) null);
	}

	public FileAuditAppender(File file) {
		this(file == null ? null : file.getPath());
	}

	public FileAuditAppender(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	protected File getFile() {
		if (this.fileName == null || this.fileName.isEmpty()) {
			return null;
		}
		return new File(this.fileName);
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	protected void onAppend(AuditEvent auditEvent, String maskedJson){
		if (this.fileName == null || this.fileName.isEmpty()) {
			throw new AuditException("Filename must be supplied");
		}

		try {

			try (FileWriter fileWriter = new FileWriter(this.fileName, true)) {
				synchronized (fileWriter) {
					try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
						printWriter.println(maskedJson);
					}
				}
			}

		} catch (Exception e) {
			throw new AuditException(e);
		}
	}

}
