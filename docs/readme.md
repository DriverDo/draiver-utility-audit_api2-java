# Audit Utility Library Documentation

## Introduction

This library allows for applications to send telementry data. A program configures an auditor and then emits audit events. Each auditor can have one or more appenders. Appenders emit the audit event to various repositories. 

**Simple Example**

```java
public class ExampleApp {

	private static final Auditor AUDITOR = AuditorFactory.createAuditor(ExampleApp.class);

	public static void main(String[] args) {

		AUDITOR.audit(new GenericAuditEvent("DO ACTION 1"));
		AUDITOR.audit(new GenericAuditEvent("DO ACTION 2"));
		AUDITOR.audit(new GenericAuditEvent("DO ACTION 3"));

	}
	
}

```


<br/>
<br/>

### Configuring Auditors
There are many ways to configure an auditor. The most simple way is to include an xml file with the name of **auditor.xml** in the class path. Below is simple example of the file. 

**Sample auditor.xml**

```xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<configuration>

	<auditors>
		<auditor declaringTypeRegEx=".*" class="com.draiver.core.utility.audit.auditor.StandardAuditor" />
	</auditors>

	<appenders>
		
		<appender name="FileAuditAppender" class="com.draiver.core.utility.audit.appender.FileAuditAppender" minLevel="DEBUG">			
			<parameters>
				<param name="fileName" value="audit-output.txt" />
			</parameters>			
		</appender>
		
		<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">
			
			<parameters>
				<param name="prettyPrint" value="[[RESOLVER]]:{class='com.draiver.core.utility.audit.configuration.Base64ConfigurationParameterResolver' value='dHJ1ZQ=='}" />
			</parameters>

			<filters>
				<filter class="com.draiver.core.utility.audit.filter.AllowAuditEventFilter">
					<parameters>
						<param name="property" value="EventName" />
						<param name="pattern" value=".*1" />
					</parameters>
				</filter>
			</filters>

			<maskers>
				<masker class="com.draiver.core.utility.audit.masker.PropertyAuditEventMasker">
					<parameters>
						<param name="propertyName" value="Message" />
						<param name="regExPattern" value="\d{3}" />
						<param name="replaceWith" value="!!!" />
					</parameters>
				</masker>
			</maskers>
			
		</appender>
		
		<appender name="FatalAuditAppender" class="com.draiver.core.utility.audit.appender.InMemoryAuditAppender" minLevel="FATAL" />
		
	</appenders>

</configuration>
```

 <br/><br/>

## Learn More
[Configuring Auditors and Appenders](configure-appenders.md)
