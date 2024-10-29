## Audit Event Filters
A filter is responsible for determining if a masker or an appender should execute its action.
To learn more about audit appenders and filters visit the [Audit Appender documentation](appenders.md). To learn more about audit maskers and filters visit the [Audit Masker documentation](appender-maskers.md)

<Br/>

**Pre-defined Filters**  
[AllowAuditEventFilter](#AllowAuditEventFilter), [DenyAuditEventFilter](#DenyAuditEventFilter) 


<Br/><Br/>

### AllowAuditEventFilter
The AllowAuditEventFilter will allow actions to be applied if a match is found. 

**Sample config**

```xml
<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">
	<filters>
		<filter class="com.draiver.core.utility.audit.filter.AllowAuditEventFilter">
			<parameters>
				<param name="property" value="EventName" />
				<param name="pattern" value=".*1" />
			</parameters>
		</filter>
	</filters>	
</appender>
```

<Br/>

**Parameters**
  

property **(OPTIONAL)**
> string - The name of the audit event property to inspect. If property is not supplied the filter will convert the event to JSON and apply the pattern to that

pattern **(REQUIRED)**
> string - The regex pattern to apply


<Br/><Br/>

### DenyAuditEventFilter
The DenyAuditEventFilter will not allow actions to be applied if a match is found. 

**Sample config**

```xml
<appender name="ConsoleAuditAppender" type="elead.audit.appender.ConsoleAuditAppender, unum-library-audit-csharp" minLevel="DEBUG">
    <parameters>
        <param name="PrettyPrint" value="false" />
    </parameters>
    <filters>
        <filter class="com.draiver.core.utility.audit.filter.DenyAuditEventFilter">
            <parameters>            
                <param name="property" value="Namespace" />
                <param name="pattern" value="elead.security.*" />            
            </parameters>
        </filter>
    </filters>
</appender>
```

<Br/>

**Parameters**
  
property **(OPTIONAL)**
> string - The name of the audit event property to inspect. If property is not supplied the filter will convert the event to JSON and apply the pattern to that

pattern **(REQUIRED)**
> string - The regex pattern to apply