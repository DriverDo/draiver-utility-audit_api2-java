package com.draiver.examples;

import java.util.UUID;

import com.draiver.core.utility.audit.auditor.Auditor;
import com.draiver.core.utility.audit.auditor.AuditorFactory;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.EventConfigImpl;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.examples.audit.events.AppStartAuditEvent;
import com.draiver.examples.audit.events.AuthenticationAuditEvent;
import com.draiver.examples.audit.events.ServiceInvokeAuditEvent;

@SuppressWarnings("squid:S2925")
public class ExampleApp {

	private static final Auditor AUDITOR = AuditorFactory.createAuditor(ExampleApp.class);

	public static void main(String[] args) {

		// create context
		AppContext context = new AppContext();
		context.setExperienceId(UUID.randomUUID().toString());
		context.setSessionId(UUID.randomUUID().toString());
		context.setBaseEventConfig(createEventConfig(context));
		AUDITOR.setEventConfig(context.getBaseEventConfig());

		AUDITOR.auditAsync(new AppStartAuditEvent("ExampleApp"));
		authenticateUser(context, "jfayling");
		
		AUDITOR.auditAsync(new GenericAuditEvent("DO ACTION 1"));
		AUDITOR.auditAsync(new GenericAuditEvent("DO ACTION 2"));
		AUDITOR.auditAsync(new GenericAuditEvent("DO ACTION 3"));

	}

	private static void authenticateUser(AppContext context, String userName) {
		EventConfig eventConfig = AuditEventUtils.clone(context.getBaseEventConfig());
		eventConfig.setTransactionId(UUID.randomUUID().toString());
		context.setCurrentEventConfig(eventConfig);

		AUDITOR.auditAsync(new AuthenticationAuditEvent(userName, AuditEventStatus.TRANSACTION_BEGIN), eventConfig);
		runAuthServiceX(context);
		AUDITOR.auditAsync(new AuthenticationAuditEvent(userName, AuditEventStatus.TRANSACTION_END_SUCCESS), eventConfig);

	}

	private static void runAuthServiceX(AppContext context) {
		EventConfig eventConfig = AuditEventUtils.clone(context.getCurrentEventConfig());
		eventConfig.setConversationId(eventConfig.getTransactionId());
		eventConfig.setTransactionId(UUID.randomUUID().toString());
		context.setCurrentEventConfig(eventConfig);

		AUDITOR.auditAsync(new ServiceInvokeAuditEvent("authServiceX", AuditEventStatus.TRANSACTION_BEGIN), eventConfig);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // simulate doing work

		runAuthServiceY(context);

		AUDITOR.auditAsync(new ServiceInvokeAuditEvent("SERVICE-X", AuditEventStatus.TRANSACTION_END_SUCCESS), eventConfig);
	}

	private static void runAuthServiceY(AppContext context) {
		EventConfig eventConfig = AuditEventUtils.clone(context.getCurrentEventConfig());
		eventConfig.setSequenceId(UUID.randomUUID().toString());

		AUDITOR.auditAsync(new ServiceInvokeAuditEvent("authServiceY", AuditEventStatus.TRANSACTION_BEGIN), eventConfig);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // simulate doing work

		AUDITOR.auditAsync(new ServiceInvokeAuditEvent("SERVICE-Y", AuditEventStatus.TRANSACTION_END_SUCCESS), eventConfig);
	}

	private static EventConfig createEventConfig(AppContext context) {
		EventConfig output = new EventConfigImpl();
		output.setAppName("ExampleApp");
		output.setExperienceId(context.getExperienceId());
		output.setSessionId(context.getSessionId());
		output.setModuleName("Console");
		output.setEnv("LOCAL");
		output.setNamespace("com.draiver.apps.simulator");
		output.setDivision("NA-US");
		return output;
	}

}
