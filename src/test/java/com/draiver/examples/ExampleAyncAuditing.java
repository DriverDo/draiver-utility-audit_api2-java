package com.draiver.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.draiver.core.utility.audit.auditor.Auditor;
import com.draiver.core.utility.audit.auditor.AuditorFactory;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.EventConfig;
import com.draiver.core.utility.audit.events.EventConfigImpl;
import com.draiver.examples.audit.events.AppStartAuditEvent;
import com.draiver.examples.audit.events.AuthenticationAuditEvent;
import com.draiver.examples.audit.events.ServiceInvokeAuditEvent;

/**
 * Example that shows how to asynchronously post audit events
 * 
 * @author jfayl
 *
 */
@SuppressWarnings("squid:S2925")
public class ExampleAyncAuditing {

	// declare a static final auditor to post events
	private static final Auditor AUDITOR = AuditorFactory.createAuditor(ExampleAyncAuditing.class);

	public static void main(String[] args) throws InterruptedException {

		// use an event config to store common event properties
		EventConfig baseEventConfig = new EventConfigImpl();
		baseEventConfig.setAppName("Simulator App");
		baseEventConfig.setModuleName("Console");
		baseEventConfig.setEnv("LOCAL");
		baseEventConfig.setNamespace("com.draiver.apps.simulator");
		baseEventConfig.setDivision("NA-US");
		baseEventConfig.setExperienceId(UUID.randomUUID().toString());
		baseEventConfig.setSessionId(UUID.randomUUID().toString());

		// if you supply an auditor with a base event config all events that are passed
		// to the auditor will get the event config values appended to them (unless the
		// event already has a property value)
		AUDITOR.setEventConfig(baseEventConfig);

		// create a list to store all the async futures supplied by the auditAsync
		// method. This way you can make sure your application does not terminate until
		// all the audit messages are processed
		List<CompletableFuture<Void>> auditFutures = new ArrayList<>();

		// here is a sample of how to emit an async audit message
		auditFutures.add(AUDITOR.auditAsync(new AppStartAuditEvent(baseEventConfig.getAppName())));

		auditFutures.add(AUDITOR.auditAsync(new AuthenticationAuditEvent("jfayling", AuditEventStatus.TRANSACTION_BEGIN)));
		auditFutures.add(AUDITOR.auditAsync(new ServiceInvokeAuditEvent("AuthenticationService", AuditEventStatus.TRANSACTION_BEGIN)));
		Thread.sleep(200); // simulate doing something
		auditFutures.add(AUDITOR.auditAsync(new ServiceInvokeAuditEvent("AuthenticationService", AuditEventStatus.TRANSACTION_END_SUCCESS)));
		auditFutures.add(AUDITOR.auditAsync(new AuthenticationAuditEvent("jfayling", AuditEventStatus.TRANSACTION_END_SUCCESS)));

		// tell the auditor to block until all aync functions have completed
		AUDITOR.awaitAyncAudits(auditFutures);

	}

}
