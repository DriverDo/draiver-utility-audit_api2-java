# Configuring Auditors and Appenders

## Introduction
You configure the auditor to use one or more appenders via an auditor config file, direct object maniupation or using spring. To learn more about the pre-defined audit appenders visit the [Audit Appender documentation](appenders.md). To learn more about auditors visit [Auditors documentation](auditors.md)

<br/>

### Getting Started
The simplest way to use the audit framework is to include an audit config file in the application class path. The name and location of the file can be anything you want. However if you want to do the minimal amount of work you should name the file auditor.xml and place it in the src/main/resources root. 

If you decide to use a different name and/or location, you need to supply the location of the file when you call createAuditor method of the AuditorFactory object. 

```java
Auditor auditor  = AuditorFactory.createAuditor("my-auditor-config-file.xml");
```

Once you have the configuration file defined you must ensure there is at least one appender defined. If the framework does not detect any appenders defined it will default to the NoOpAuditAppender which will ignore all audit attempts.

**Simple auditor.xml example**

```xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<configuration>

	<auditors>
		<auditor declaringTypeRegEx=".*" class="com.draiver.core.utility.audit.auditor.StandardAuditor" />
	</auditors>
	
	<appenders>
		<appender name="InMemoryAuditAppender" class="com.draiver.core.utility.audit.appender.InMemoryAuditAppender" minLevel="DEBUG">
			<parameters>
				<param name="maxMessages" value="100" />
			</parameters>
		</appender>
	</appenders>

</configuration>
```


<br/>

### auditors Element
This xml element tells the application to support one or more auditors. This section determines what auditor to return when the createAuditor method is fired from the AuditorFactory. If this section is not supplied the AuditorFactory will always return an instance of StandardAuditor. 

**Supported Child Elements**  
  
[auditor](#auditor-Element) 

<br/>

### auditor Element
This xml element supplies a rule definination for the AuditorFactory for determining what auditor to return when the createAuditor method is called. If this section is not supplied the AuditorFacotry will return StandardAuditor. When the createAuditor method is fired the AuditorFactory will inspect the declaring type supplied by the calling application against the list of auditors. The AuditorFactory will apply the regex patterns supplied in the declaringTypeRegEx attribute attempting to find a match. The first match will return the auditor declared in the class attribute of the auditor element that was a match. 

**Example**

```xml
	<auditors>
		<auditor declaringTypeRegEx=".*" class="com.draiver.core.utility.audit.auditor.StandardAuditor" />
	</auditors>
```

**Attributes**

declaringTypeRegEx **(REQUIRED)**
> The regex pattern to use when inspecting the declaring type supplied by the createAuditor method.

class **(REQUIRED)**
> The class path to the auditor to create when a match is found

<br/>

### appenders Element
This xml element tells the application to support one or more appenders.

**Supported Child Elements**    
[appender](#appender-Element)

<br/>

### appender Element
This xml element tells the application to support a specific audit appender. An appender is responsible for persisting an audit event message. There are several pre-defined appenders already built in the framework. You can also build new ones. To learn more about appenders visit the [Audit Appenders documentation](appenders.md)

**Example**

```xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<configuration>

	<auditors>
		<auditor declaringTypeRegEx=".*" class="com.draiver.core.utility.audit.auditor.StandardAuditor" />
	</auditors>
	
	<appenders>
		
		<appender name="FileAuditAppender" class="com.draiver.core.utility.audit.appender.FileAuditAppender" minLevel="DEBUG">		
			<parameters>
				<param name="fileName" value="auditor-output.txt" />
			</parameters>			
		</appender>
		
		<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">
			
			<parameters>
				<param name="prettyPrint" value="[[RESOLVER]]:{class='com.draiver.core.utility.audit.configuration.Base64ConfigurationParameterResolver' value='dHJ1ZQ=='}" />
				<!-- <param name="prettyPrint" value="false" /> -->
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

**Attributes**

name **(REQUIRED)**
> Name of the appender being configured

class **(REQUIRED)**
> The classpath to the audit appender that should be created

minLevel 
> The minium audit EventLevel value required to append an audit message. The default value is DEBUG. Possible values are FATAL, ERROR, WARN, INFO and DEBUG. <br/><br/> The appender will append any message that has an event level less than or equal to what is set here. FATAL is the lowest level and DEBUG is the highest.

**Supported Child Elements**  
[parameters](#parameters-Element), [maskers](#maskers-Element), [filters](#filters-Element)  

<br/>


### parameters Element
This xml element sets one or more properties for its parent element/object

**Supported Child Elements**  
[param](#param-Element)

**Supported Parent Elements**  
[appender](#appender-Element), [filter](#filter-Element), [masker](#masker-Element) 

<br/>



### param Element
This xml element sets a single property for the parent element/object

**Example**

```xml
<appender name="FileAuditAppender" class="com.draiver.core.utility.audit.appender.FileAuditAppender" minLevel="DEBUG">		
	<parameters>
		<param name="fileName" value="auditor-output.txt" />
	</parameters>			
</appender>
```

<Br/>

**Attributes**  

name **(REQUIRED)**
> Name of the property to set


value **(REQUIRED)**
> Value for the property
> 
> **Parameter Resolvers**
>>
>> If the value of a parameter starts with [[RESOLVER]] and conforms to the regex pattern of <br/><br/> ``\[\[RESOLVER\]\]\:\{\s*class\s*=\s*'(.*?)'\s*value\s*=\s*'(.*?)'\s*\}`` <br/><br/> then a parameter resolver will be executed when return the value of the parameter to the AuditorFactory. Parameter resolvers can be used to obfuscate the values stored in the configuration file. The audit framework supports the Base64ConfigruationParameterResolver class for simple base64 encoding. You can create your own parameter resolver by implementing the ConfigurationParameterResolver interface. <br/><br/> **Example** <br/>
>> ``<param name="prettyPrint" value="[[RESOLVER]]:{class='com.draiver.core.utility.audit.configuration.Base64ConfigurationParameterResolver' value='dHJ1ZQ=='}" />``  


**Supported Child Elements**  
None 

**Supported Parent Elements**  
[parameters](#parameters-Element)

<br/>

### maskers Element
This xml element defines one or more maskes assigned to an appender

**Supported Child Elements**  
[masker](#masker-Element) 

**Supported Parent Elements**  
[appender](#appender-Element) 


### masker Element
This xml element defines a masker to be assigned to an appender. A masker is responsible for redacting data before it is appended. There are several different pre-defined maskers. To learn more about maskers go to [Audit Appender Maskers Documentation](appender-maskers.md)

**Supported Child Elements**  
[parameters](#parameters-Element)
 

**Supported Parent Elements**  
[appender](#appender-Element) 

<br/>

### filters Element
This xml element defines one or more filters assigned to a parent

**Supported Child Elements**  
[filter](#filter-Element) 

**Supported Parent Elements**  
[appender](#appender-Element), [masker](#masker-Element) 

### filter Element
This xml element defines a filter to be assigned to a parent. A filter is responsible for determining if an audit event message should be appended. There are several different pre-defined filters. To learn more about filters go to [Audit Appender Filters Documentation](appender-filters.md)

**Supported Child Elements**
[parameters](#parameters-Element) 

**Supported Parent Elements**  
[filters](#filter-Element) 