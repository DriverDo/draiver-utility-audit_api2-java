package com.draiver.core.utility.audit.events;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

import org.apache.commons.beanutils.BeanUtils;

public class EventConfigImpl implements EventConfig {

	private String appName;
	private String moduleName;
	private String env;
	private String division;
	private String machine;
	private String namespace;
	private String conversationId;
	private String sessionId;
	private String experienceId;
	private String transactionId;
	private String sequenceId;
	private String region = "";
	private String tenantId = "";
	private String partnerId = "";

	public EventConfigImpl() {
		try {
			setMachine(java.net.InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// do nothing
		}
	}

	public EventConfigImpl(EventConfig copyFrom) {
		try {
			BeanUtils.copyProperties(this, copyFrom);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// eat on purpose
		}
	}

	@Override
	public String getAppName() {
		return appName;
	}

	@Override
	public String getConversationId() {
		return conversationId;
	}

	@Override
	public String getDivision() {
		return division;
	}

	@Override
	public String getEnv() {
		return env;
	}

	@Override
	public String getExperienceId() {
		return experienceId;
	}

	@Override
	public String getMachine() {
		return machine;
	}

	@Override
	public String getModuleName() {
		return moduleName;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getRegion() {
		return region;
	}

	@Override
	public String getSequenceId() {
		return sequenceId;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	public String getTenantId() {
		return tenantId;
	}

	@Override
	public String getTransactionId() {
		return transactionId;
	}

	@Override
	public void setAppName(String value) {
		appName = value;
	}

	@Override
	public void setConversationId(String value) {
		conversationId = value;
	}

	@Override
	public void setDivision(String value) {
		division = value;
	}

	@Override
	public void setEnv(String value) {
		env = value;
	}

	@Override
	public void setExperienceId(String value) {
		experienceId = value;
	}

	@Override
	public void setMachine(String value) {
		machine = value;
	}

	@Override
	public void setModuleName(String value) {
		moduleName = value;
	}

	@Override
	public void setNamespace(String value) {
		namespace = value;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public void setSequenceId(String value) {
		sequenceId = value;
	}

	@Override
	public void setSessionId(String value) {
		sessionId = value;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public void setTransactionId(String value) {
		transactionId = value;
	}

}
