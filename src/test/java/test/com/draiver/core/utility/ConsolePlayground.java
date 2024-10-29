package test.com.draiver.core.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.draiver.core.utility.audit.appender.ConsoleAuditAppender;
import com.draiver.core.utility.audit.appender.FileAuditAppender;
import com.draiver.core.utility.audit.appender.InMemoryAuditAppender;
import com.draiver.core.utility.audit.appender.RollingFileAuditAppender;
import com.draiver.core.utility.audit.auditor.Auditor;
import com.draiver.core.utility.audit.auditor.AuditorFactory;
import com.draiver.core.utility.audit.auditor.StandardAuditor;
import com.draiver.core.utility.audit.configuration.XmlAuditorConfigurationManager;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.filter.AuditEventFilter;
import com.draiver.core.utility.audit.masker.AuditEventMasker;
import com.draiver.core.utility.audit.masker.SensitiveDataAuditEventMasker;

import test.com.draiver.core.utility.audit.events.SampleAuditEvent;

public class ConsolePlayground {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// runTest1();
		// runTest2();
		// runTest3();
		// runTest4();
		//runTest5();
		runTest6();

	}

	private static void runTest6() {
		Auditor auditor = AuditorFactory.createAuditor(ConsolePlayground.class, "auditor-consolePlayground.xml");
		
		for (int i = 0; i < 10; i++) {
			AuditEvent tmpEvent = new GenericAuditEvent("EVENT" + i);
			if (i == 0) {
				tmpEvent.setEventLevel(AuditEventLevel.FATAL);
			}
			auditor.auditAsync(tmpEvent);
		}
		
		System.out.println("DONE");
	}

	private static void runTest5() {
		Auditor auditor = new StandardAuditor();
		auditor.configure(new XmlAuditorConfigurationManager());

		for (int i = 0; i < 10; i++) {
			AuditEvent tmpEvent = new GenericAuditEvent("EVENT" + i);
			if (i == 0) {
				tmpEvent.setEventLevel(AuditEventLevel.FATAL);
			}
			auditor.auditAsync(tmpEvent);
		}

		InMemoryAuditAppender inMemoryAppender = (InMemoryAuditAppender) auditor.getAppenders().stream().filter(appender -> {
			return appender instanceof InMemoryAuditAppender;
		}).findFirst().get();

		System.out.println(inMemoryAppender.getMessages().size());

		AuditEvent tmpEvent = new GenericAuditEvent("Masker1");
		tmpEvent.setMessage("My number is 123");
		auditor.auditAsync(tmpEvent);

	}

	private static void runTest4() {
		List<String> excludeList = new ArrayList<>();
		excludeList.add("test.com.draiver.core.utility");
		excludeList.add("java.lang.Thread");

		AuditEventUtils.fetchFilteredStackTrace(excludeList).forEach(x -> {
			System.out.println(x);
		});

	}

	private static void runTest3() {
		InMemoryAuditAppender appender = new InMemoryAuditAppender(10000);
		// FileAuditAppender appender = new FileAuditAppender("d:/tmp/jason.txt");
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			futures.add(appender.appendAsync(new GenericAuditEvent("EVENT" + i)));
		}

		CompletableFuture<Void> asyncAction = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

		try {
			asyncAction.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		int counter = 0;
		for (CompletableFuture<Void> future : futures) {
			System.out.println(counter + ": " + future.isDone());
			counter++;
		}

		System.out.println(appender.getMessages().size());
		System.out.println("DONE");

	}

	private static void runTest2() throws InterruptedException, ExecutionException {
		StandardAuditor auditor = new StandardAuditor();
		ConsoleAuditAppender appender1 = new ConsoleAuditAppender();
		InMemoryAuditAppender appender2 = new InMemoryAuditAppender();
		FileAuditAppender appender3 = new FileAuditAppender("d:/tmp/jason2.txt");

		auditor.getAppenders().add(appender1);
		auditor.getAppenders().add(appender2);
		auditor.getAppenders().add(appender3);

		System.out.println("--------------SYNC OUTPUT----------------------");
		for (int i = 0; i < 10; i++) {
			auditor.auditAsync(new GenericAuditEvent("EVENT1"));
		}
		System.out.println(appender2.getMessages().size());

		System.out.println("--------------ASYNC OUTPUT----------------------");
		CompletableFuture<Void> asyncAction = null;
		List<AuditEvent> auditEvents = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			auditEvents.add(new GenericAuditEvent("EVENT2"));
		}
		asyncAction = auditor.auditAsync(auditEvents);
		asyncAction.get();
		System.out.println(appender2.getMessages().size());

	}

	private static void runTest1() {
		SampleAuditEvent event1 = new SampleAuditEvent();
		event1.setAppName("JASON");
		event1.setParameter1("Param 1");
		event1.setParameter2("Param 2");
		event1.setMessage("My SSN is 123-45-6789 and you shouldn't be able to see it");

		List<AuditEventMasker> maskers = new ArrayList<>();
		maskers.add(new SensitiveDataAuditEventMasker());

		List<AuditEventFilter> filters = new ArrayList<>();
		// filters.add(new AllowAuditEventFilter("AppName", "JASON"));

//		AuditAppender appender = new ConsoleAuditAppender(false, true);
		// FileAuditAppender appender = new FileAuditAppender("d:/tmp/fun/jason.txt");
		RollingFileAuditAppender appender = new RollingFileAuditAppender("d:/tmp/fun/jason.txt", 1024);
		appender.setMaskers(maskers);
		appender.setFilters(filters);

		CompletableFuture<Void> asyncAction = null;
		try {
			System.out.println("--------------SYNC OUTPUT OF 100----------------------");
			for (int i = 0; i < 1000; i++) {
				appender.append(event1);
			}

			System.out.println("--------------ASYNC OUTPUT OF 100----------------------");
			List<CompletableFuture<?>> futures = new ArrayList<>();
			for (int i = 0; i < 1000; i++) {
				AuditEvent event2 = new GenericAuditEvent("EVENT2");
				futures.add(appender.appendAsync(event2));
			}

			asyncAction = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			asyncAction.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("DONE");
		// CommonConsoleUtil.readLine("PRESS ENTER TO EXIT");

	}

}
