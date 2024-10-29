package com.draiver.examples;

import com.draiver.core.utility.audit.events.EventConfig;

public class AppContext {

	private String sessionId;
	private String experienceId;
	private EventConfig baseEventConfig;
	private EventConfig currentEventConfig;

	public EventConfig getBaseEventConfig() {
		return baseEventConfig;
	}

	public void setBaseEventConfig(EventConfig baseEventConfig) {
		this.baseEventConfig = baseEventConfig;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getExperienceId() {
		return experienceId;
	}

	public void setExperienceId(String experienceId) {
		this.experienceId = experienceId;
	}

	public EventConfig getCurrentEventConfig() {
		return currentEventConfig;
	}

	public void setCurrentEventConfig(EventConfig currentEventConfig) {
		this.currentEventConfig = currentEventConfig;
	}

}
