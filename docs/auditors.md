## Auditors
An auditor is responbile for coordinating the task of persisting an audit event message. Each auditor can have one or more parameters associated to it. You can either create the auditor via java code, or you configure the auditor inside an auditor config file. To learn more about configuring appenders visit the [Configuring Auditors and Appenders documentation](configure-appenders.md).
<br/><br/>
You can also create your own auditors. All you need to do is create a class and have it extend AuditorBase.
<br/><br/>

<br/>


**Pre-defined Auditors**  
[StandardAuditor](#StandardAuditor) 


<br/>

### StandardAuditor

This is a general purpose Auditor that should work for must use cases. For asynchronous tasks, this auditor uses the CompletableFuture concept for threading.   