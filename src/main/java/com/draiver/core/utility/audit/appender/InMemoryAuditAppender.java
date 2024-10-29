package com.draiver.core.utility.audit.appender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.draiver.core.utility.audit.events.AuditEvent;

public class InMemoryAuditAppender extends AuditAppenderBase {

	private static final double DEFAULT_MAX_MESSAGES = 10000;

	private List<String> messages = Collections.synchronizedList(new ArrayList<>());
	private double maxMessages = DEFAULT_MAX_MESSAGES;

	public InMemoryAuditAppender() {
		this(DEFAULT_MAX_MESSAGES);
	}

	public InMemoryAuditAppender(double maxMessages) {
		this.maxMessages = maxMessages;
		this.messages = new ArrayList<>();
	}

	public void clearMessages() {
		this.messages.clear();
	}

	public double getMaxMessages() {
		return this.maxMessages;
	}

	public List<String> getMessages() {
		return this.messages;
	}

	@Override
	protected void onAppend(AuditEvent auditEvent, String maskedJson) {
		if (this.messages.size() >= this.maxMessages) {
			this.messages.remove(0);
		}
		synchronized (this) {
			this.messages.add(maskedJson);
		}
	}

	public void setMaxMessages(double value) {
		this.maxMessages = value;
	}

}
