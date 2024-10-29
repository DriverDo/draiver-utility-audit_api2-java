package test.com.draiver.core.utility.audit.appender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.draiver.core.utility.audit.appender.AuditAppender;
import com.draiver.core.utility.audit.appender.FileAuditAppender;
import com.draiver.core.utility.audit.events.AuditEvent;
import com.draiver.core.utility.audit.events.AuditEventLevel;
import com.draiver.core.utility.audit.events.AuditEventStatus;
import com.draiver.core.utility.audit.events.AuditEventUtils;
import com.draiver.core.utility.audit.events.GenericAuditEvent;
import com.draiver.core.utility.audit.exception.AuditException;
import com.draiver.core.utility.audit.filter.AllowAuditEventFilter;
import com.draiver.core.utility.audit.masker.SensitiveDataAuditEventMasker;

class FileAuditAppenderTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFileAuditAppender01() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 5);
	}

	@Test
	void testFileAuditAppenderFATAL() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());
		appender.setMinLevel(AuditEventLevel.FATAL);

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 1);

	}

	@Test
	void testFileAuditAppenderERROR() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());
		appender.setMinLevel(AuditEventLevel.ERROR);

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 2);

	}

	@Test
	void testFileAuditAppenderWARN() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());
		appender.setMinLevel(AuditEventLevel.WARN);

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 3);

	}

	@Test
	void testFileAuditAppenderINFO() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());
		appender.setMinLevel(AuditEventLevel.INFO);

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 4);

	}

	@Test
	void testFileAuditAppenderDEBUG() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());
		appender.setMinLevel(AuditEventLevel.DEBUG);

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 5);

	}

	@Test
	void testMasker() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());
		appender.setMinLevel(AuditEventLevel.DEBUG);
		appender.getMaskers().add(new SensitiveDataAuditEventMasker());

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 5);
		
		String data = fetchFileContents(tmpFile);
		assertFalse(data.contains("123-45-6789"));

	}
	
	@Test
	void testFilter() throws IOException, AuditException {
		File tmpFile = File.createTempFile("temp", ".log");
		tmpFile.deleteOnExit();

		FileAuditAppender appender = new FileAuditAppender(tmpFile.getPath());
		appender.setMinLevel(AuditEventLevel.DEBUG);
		appender.getFilters().add(new AllowAuditEventFilter("AppName", "APP2"));

		createTempFile(tmpFile, appender);
		assertValidTempFile(tmpFile, 2);
	}

	private void createTempFile(File tmpFile, AuditAppender appender) throws AuditException {
		tmpFile.delete();

		AuditEvent event0 = new GenericAuditEvent("EVENT0", AuditEventLevel.FATAL, AuditEventStatus.SUCCESS);
		event0.setAppName("APP1");

		AuditEvent event1 = new GenericAuditEvent("EVENT1", AuditEventLevel.ERROR, AuditEventStatus.SUCCESS);
		event1.setAppName("APP1");

		AuditEvent event2 = new GenericAuditEvent("EVENT2", AuditEventLevel.WARN, AuditEventStatus.SUCCESS);
		event2.setAppName("APP1");
		event2.setMessage("My SSN is 123-45-6789");

		AuditEvent event3 = new GenericAuditEvent("EVENT3", AuditEventLevel.INFO, AuditEventStatus.SUCCESS);
		event3.setAppName("APP2");

		AuditEvent event4 = new GenericAuditEvent("EVENT4", AuditEventLevel.DEBUG, AuditEventStatus.SUCCESS);
		event4.setAppName("APP2");

		appender.append(event0);
		appender.append(event1);
		appender.append(event2);
		appender.append(event3);
		appender.append(event4);
	}

	private void assertValidTempFile(File tmpFile, int maxLines) throws FileNotFoundException, IOException {
		int counter = 0;
		try (FileReader fileReader = new FileReader(tmpFile.getPath())) {
			try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
				String line = bufferedReader.readLine();
				while (line != null) {
					AuditEvent tmpEvent = AuditEventUtils.fromJson(line);
					assertTrue(tmpEvent.getEventName().startsWith("EVENT"));

					counter++;
					line = bufferedReader.readLine();
				}
			}
		}

		assertEquals(maxLines, counter);
	}
	
	private String fetchFileContents(File tmpFile) throws IOException {
		Path path = Paths.get(tmpFile.getParent(), tmpFile.getName());
		List<String> lines = Files.readAllLines(path);
		return String.join("\n", lines);
	}

}
