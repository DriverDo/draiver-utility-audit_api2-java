## Appender Parameter Resolvers
Appenders frequently will have parameters that can be set within the config file. In some cases the value that can be set into the appender parameter needs to be a dynamic value
or an encrypted value. To solve this issues the appender supports the concept of a parameter resolver. There are several pre-defined parameter resolvers that you can choose from.


You can also create your own resolvers. All you need to do is implement the IParameterConfigurationElementResolver interface.



**Sample config**

```xml
<appender name="ConsoleAuditAppender" class="com.draiver.core.utility.audit.appender.ConsoleAuditAppender" minLevel="DEBUG">			
	<parameters>
		<param name="prettyPrint" value="[[RESOLVER]]:{class='com.draiver.core.utility.audit.configuration.Base64ConfigurationParameterResolver' value='dHJ1ZQ=='}" />
	</parameters>	
</appender>
```

<br/>

To implement enable a parameter resolver the value attribute must have a string value the conforms to the below regex pattern:

**Regex**

```text
\[\[RESOLVER\]\]:{\s*type='(.*?)'\s*value='(.*?)'\s*}
```

<br/>

**Sample Resolver**

```text
[[RESOLVER]]:{class='com.draiver.core.utility.audit.configuration.Base64ConfigurationParameterResolver' value='dHJ1ZQ=='}
```

<br/>

The regex pattern is broken down into two separate sections. 

**Parameters**

class **(REQUIRED)**
> The class path of the ConfiguraitonParameterResolver to create

value **(REQUIRED)**
> The value the resolver will attempt to resolve

<br/><br/>

**Pre-defined Parameter Resolvers**  

[Base64DecodeParameterConfigurationElementResolver](#Base64DecodeParameterConfigurationElementResolver) 


<br/><br/>


#### Base64DecodeParameterConfigurationElementResolver

This resolver will apply a Base64 decoder to the contents supplied in the regex value section. Use can use this resolver to obsifacte semi-sensitive data. 