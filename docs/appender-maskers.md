## Audit Appender Maskers
A masker is responsible for redacting an audit event message before it is persisted by the audit appender. To learn more about audit appenders visit the [Audit Appender documentation](appenders.md)

Maskers can have one or more filters assigned to them. A filter determines if the masker should be executed. To learn more about filters visit the [Masker Filter documentation](appender-filters.md).

<Br/>

**Pre-defined Maskers**  

[PropertyAuditEventMasker](#PropertyAuditEventMasker), [RegExAuditEventMasker](#RegExAuditEventMasker), [SensitiveDataAuditEventMasker](#SensitiveDataAuditEventMasker) 

<Br/>
<Br/>

### PropertyAuditEventMasker
The PropertyAuditEventMasker will redact a specific audit event property. It will apply the supplied regex replacement on matches found. 

**Sample config**

```xml
<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">
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
```

<Br/>
<Br/>

**Parameters**  

propertyName **(REQUIRED)** 
> string - The name of the audit event property to inspect

regExPattern **(REQUIRED)**
> string - The regex pattern to apply

replaceWith **(REQUIRED)**
> string - The string to replace with any matches found.


<br/><br/>

### RegExAuditEventMasker
The RegExAuditEventMasker inspects the entire json output of the event message with the supplied regex pattern. if a match is found the match is replaced with the supplied replacement value

**Sample config**

```xml
<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">
	<maskers>
		<masker class="com.draiver.core.utility.audit.masker.RegExAuditEventMasker">
			<parameters>
                <param name="pattern" value=" userName - .*? " />
                <param name="replaceWith" value=" userName - redacted" />
            </parameters>
		</masker>
	</maskers>		
</appender>
```

<Br/><Br/>

**Parameters**  

pattern **(REQUIRED)** 
> string - The regex pattern to apply

replaceWith **(REQUIRED)** 
> string - The string to replace with any matches found.

<br/><br/>

### SensitiveDataAuditEventMasker
The SensitiveDataAuditEventMasker inspects the entire json output of the event message looking for sensitive data. If any is found it replaces with the supplied value. Currently this masker will look for social security numbers and credit cards.

**Sample config**

```xml
<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">
	<maskers>
		<masker class="com.draiver.core.utility.audit.masker.SensitiveDataAuditEventMasker">
			<parameters>
                <param name="replaceWith" value="*redacted*" />
            </parameters>
		</masker>
	</maskers>		
</appender>
```


<Br/>

**Parameters**  

replaceWith **(REQUIRED)** 
> string - The string to replace with any matches found.