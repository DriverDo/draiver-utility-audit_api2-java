## Audit Appenders
An audit appender is responbile for persisting an audit event message. Each appender can have one or more parameters associated to it. You can either create the appender via java code, or you configure the appender inside an configuration file. To learn more about configuring appenders visit the [Configuring Appenders documentation](configure-appenders.md).
<br/><br/>
I can also create your own audit appenders. All you need to do is create a class and have it extend AuditAppenderBase
<br/><br/>
An appender can also have one or more filters and maskers. A filter is responsible for determining if an audit event message should be persisted. A masker is responsible for redacting the data before it gets persisted. To learn more visit the following documentation:
<br/>
* [Filter Documentation](appender-filters.md)
* [Masker Documentation](appender-maskers.md)

<br/>

Appender parameters can also support custom parameter resolvers. You can use this concept to inject dynamic values into appender parameters. To learn more vist the following documentation:
<br/>
* [Parameter Resolver Documentation](appender-parameter-resolvers.md)


**Pre-defined Appenders**  
[ConsoleAuditAppender](#ConsoleAuditAppender), [FileAuditAppender](#FileAuditAppender), [InMemoryAuditAppender](#InMemoryAuditAppender), [NoOpAuditAppender](#NoOpAuditAppender), [RollingFileAuditAppender](#RollingFileAuditAppender) 
 

<br/>
<br/>


<br/>

### ConsoleAuditAppender
The ConsoleAuditAppender persists all audit event messages as JSON into the console window. 

**Sample config**

```xml
<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">		
	<parameters>				
		<param name="prettyPrint" value="true" />
	</parameters>
</appender>
```

<br/>

**Parameters**  

prettyPrint 
> Boolean - Determines if the JSON should be formatted. Default value is false

<br/>
<br/>

### FileAuditAppender
The FileAuditAppender persists all audit event messages as JSON into a file. If the file does not exist, it will be created. If the file already exists new messages will be appended into it. If a path is not specified the path will be the location where the app is executing.

**Sample config**

```xml
<appender name="FileAuditAppender" class="com.draiver.core.utility.audit.appender.FileAuditAppender" minLevel="DEBUG">
	<parameters>
		<param name="fileName" value="auditor-output.txt" />
	</parameters>	
</appender>
```

<br/>

**Parameters**  

filename **(REQUIRED)**
> String - Name or FilePath of the file to persit to.

<br/>
<br/>

### InMemoryAuditAppender
The InMemoryAuditAppender persists all audit event messages as JSON into a List\<string\> collection. You can use this appender if you want to interact with the messages that are emitted. The appender has a definable max number of messages it will retain. The default is 10,000 messages. If the max number of messages are meet the appender will drop the oldest message at each insert of a new record.

**Sample config**

```xml
<appender name="InMemoryAuditAppender" class="com.draiver.core.utility.audit.appender.InMemoryAuditAppender" minLevel="DEBUG">
	<parameters>
		<param name="maxMessages" value="100" />
	</parameters>
</appender>
```

<br/>

**Sample code**

```java
InMemoryAuditAppender appender = new InMemoryAuditAppender(10);
appender.append(new GenericAuditEvent("EVENT0", "TEST", AuditEventLevel.FATAL, AuditEventStatus.SUCCESS));
appender.append(new GenericAuditEvent("EVENT1", "TEST", AuditEventLevel.ERROR, AuditEventStatus.SUCCESS));
appender.append(new GenericAuditEvent("EVENT2", "TEST", AuditEventLevel.WARN, AuditEventStatus.SUCCESS));
appender.append(new GenericAuditEvent("EVENT3", "TEST", AuditEventLevel.INFO, AuditEventStatus.SUCCESS));
appender.append(new GenericAuditEvent("EVENT4", "TEST", AuditEventLevel.DEBUG, AuditEventStatus.SUCCESS));

System.out.println(appender.getMessages().size());
```

<Br/>

**Parameters**  

maxMessages
> double - Total number of messages to retain. Default is 10,000

<br/><br/>

### NoOpAuditAppender
The NoOpAuditAppender drops all messages that sent to it. Use this in situations where an appender is required but you don't want to append.

<br/>
<br/>

### RollingFileAuditAppender
The RollingFileAuditAppender persists all audit event messages as JSON into a file. If the file does not exist, it will be created. If a path is not specified the path will be the location where the app is executing. When the file is created the appender will use the name supplied and then append a timestamp. If the size of the file grows bigger than the defined size a new file is created with a new timestamp.

**Sample config**

```xml
<appender name="RollingFileAuditAppender" class="com.draiver.core.utility.audit.appender.RollingFileAuditAppender" minLevel="DEBUG">
	<parameters>
		<param name="BaseFilename" value="jason.log" />
        	<param name="MaxFileSize" value="5242880" />
	</parameters>	
</appender>
```

<br/>

**Parameters**
  
BaseFilename **(REQUIRED)**
>  String - Name or FilePath of the file to persit to.


MaxFileSize **(REQUIRED)**
> int - Maximum size in bytes the file can grow before a new one is created.

<br/><br/>
