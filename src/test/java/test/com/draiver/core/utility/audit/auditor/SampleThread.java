package test.com.draiver.core.utility.audit.auditor;

import java.time.ZonedDateTime;

import com.draiver.core.utility.audit.appender.InMemoryAuditAppender;
import com.draiver.core.utility.audit.auditor.StandardAuditor;
import com.draiver.core.utility.audit.events.StopwatchAuditEvent;
import com.draiver.examples.audit.events.ServiceInvokeAuditEvent;

public class SampleThread extends Thread {

	StandardAuditor auditor = new StandardAuditor();
	InMemoryAuditAppender appender1 = new InMemoryAuditAppender();
	
	public SampleThread() {
		auditor.getAppenders().add(appender1);	
	}

	@Override
	public void run() {
		StopwatchAuditEvent stopWatchEvent = new StopwatchAuditEvent(ZonedDateTime.now());		
		ServiceInvokeAuditEvent serviceEvent = new ServiceInvokeAuditEvent("SampleThread");		
		auditor.auditAsync(serviceEvent);
		auditor.auditAsync(stopWatchEvent);
	}

}
