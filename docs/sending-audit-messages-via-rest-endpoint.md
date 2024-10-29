# Sending Audit Messages via REST Endpoint

* Add unum-library-audit-csharp as a reference to your web application
* Install Microsoft.AspNet.WebApi.Cors NuGet package to support CORS
* To enable CORS support insert the below code fragment into the WebApiConfig.cs in the App_Start folder

```c#
        public static void Register(HttpConfiguration config)
        {
            // Web API configuration and services

            // Web API routes
            config.MapHttpAttributeRoutes();
            config.EnableCors();

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );
        }
```
_
* Insert the below code fragment into the Application_Start method

```c#
        protected void Application_Start()
        {
            GlobalConfiguration.Configure(WebApiConfig.Register);

            //Find the audit controller
            var assemblyResolver = new EleadAssemblyResolver(typeof(AuditController));
            GlobalConfiguration.Configuration.Services.Replace(typeof(IAssembliesResolver), assemblyResolver);
        }
```


</br>
</br>

# Calling Audit Endpoint
Once you do the above steps your web application will automaticly support the following REST endpoint. This endpoint will allow you to send multiple audit events.

**SAMPLE REQUEST**
```bash
curl -X POST \
  http://<server>/audit/emitBatch \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: ff687c18-46e7-4da8-b35d-08e395f94562' \
  -d '[
	{"EventId":"37836bb7-d26b-48b8-bc68-a29ae37e2260","EventType":"AppStartAuditEvent","EventName":"APP_START","EventSource":"unum_utility_audit_console.Program","EventCategory":"BOOTSTRAP","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150800","EventTimestamp":"636625984805065851","EventStatus":"SUCCESS","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":null,"SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":null,"Message":""},
	{"EventId":"fb6a878d-9b5e-4af7-811d-f045295c9142","EventType":"UserAuthenticatingAuditEvent","EventName":"USER_AUTHENTICATING","EventSource":"unum_utility_audit_console.Program","EventCategory":"AUTHENTICATION","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150800","EventTimestamp":"636625984807712426","EventStatus":"TRANSACTION_BEGIN","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":null,"SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"b20ab998-031c-4ba2-9ff4-73ef8bed0229","Message":"","UserId":"jfayling"},
	{"EventId":"e654319c-119b-4dd2-9c50-734791a8503d","EventType":"ServiceCallAuditEvent","EventName":"SERVICE_CALL","EventSource":"unum_utility_audit_console.Program","EventCategory":"UNKNOWN","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150800","EventTimestamp":"636625984807741729","EventStatus":"TRANSACTION_BEGIN","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":"b20ab998-031c-4ba2-9ff4-73ef8bed0229","SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"9ce44eea-59b1-454a-bf3f-1d2118dafb4a","Message":"","ServiceName":"Sip.Authentication"},
	{"EventId":"4e8499f7-9093-476f-be45-fc083d0d3340","EventType":"ServiceCallAuditEvent","EventName":"SERVICE_CALL","EventSource":"unum_utility_audit_console.Program","EventCategory":"UNKNOWN","EventVersion":"1.0","EventLevel":"WARN","EventCreated":"20180522150800","EventTimestamp":"636625984807751534","EventStatus":"TRANSACTION_END_FAIL","EventStatusDescription":"user id found, invalid password","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":"b20ab998-031c-4ba2-9ff4-73ef8bed0229","SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"9ce44eea-59b1-454a-bf3f-1d2118dafb4a","Message":"","ServiceName":"Sip.Authentication"},
	{"EventId":"d9b6171d-dfca-4639-be8c-6148867cd578","EventType":"UserAuthenticatingAuditEvent","EventName":"USER_AUTHENTICATING","EventSource":"unum_utility_audit_console.Program","EventCategory":"AUTHENTICATION","EventVersion":"1.0","EventLevel":"WARN","EventCreated":"20180522150800","EventTimestamp":"636625984807771036","EventStatus":"TRANSACTION_END_FAIL","EventStatusDescription":"Invalid Username/Password","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":null,"SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"b20ab998-031c-4ba2-9ff4-73ef8bed0229","Message":"","UserId":"jfayling"},
	{"EventId":"715af5c5-101c-472e-ac24-3aa11f0c3694","EventType":"UserAuthenticatingAuditEvent","EventName":"USER_AUTHENTICATING","EventSource":"unum_utility_audit_console.Program","EventCategory":"AUTHENTICATION","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150800","EventTimestamp":"636625984807800332","EventStatus":"TRANSACTION_BEGIN","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":null,"SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"83f233f3-b773-474b-bda8-65535e4c36fe","Message":"","UserId":"jfayling"},
	{"EventId":"c135244c-8b0c-4b16-9a1c-af01d87d1204","EventType":"ServiceCallAuditEvent","EventName":"SERVICE_CALL","EventSource":"unum_utility_audit_console.Program","EventCategory":"UNKNOWN","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150800","EventTimestamp":"636625984808308190","EventStatus":"TRANSACTION_BEGIN","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":"83f233f3-b773-474b-bda8-65535e4c36fe","SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"9ce44eea-59b1-454a-bf3f-1d2118dafb4a","Message":"","ServiceName":"Sip.Authentication"},
	{"EventId":"e87dcade-4237-4edb-91fc-f020b4c35be5","EventType":"ServiceCallAuditEvent","EventName":"SERVICE_CALL","EventSource":"unum_utility_audit_console.Program","EventCategory":"UNKNOWN","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150800","EventTimestamp":"636625984809157798","EventStatus":"TRANSACTION_END_SUCCESS","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":"83f233f3-b773-474b-bda8-65535e4c36fe","SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"9ce44eea-59b1-454a-bf3f-1d2118dafb4a","Message":"","ServiceName":"Sip.Authentication"},
	{"EventId":"7f65cd92-d837-4c21-9ec1-312724dbd71c","EventType":"UserAuthenticatingAuditEvent","EventName":"USER_AUTHENTICATING","EventSource":"unum_utility_audit_console.Program","EventCategory":"AUTHENTICATION","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150800","EventTimestamp":"636625984809646116","EventStatus":"TRANSACTION_END_SUCCESS","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":null,"SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":"83f233f3-b773-474b-bda8-65535e4c36fe","Message":"","UserId":"jfayling"},
	{"EventId":"d199b70e-152a-4b14-8d76-a8c41b65e4fa","EventType":"UserAuthenticatedAuditEvent","EventName":"USER_AUTHENTICATED","EventSource":"unum_utility_audit_console.Program","EventCategory":"AUTHENTICATION","EventVersion":"1.0","EventLevel":"INFO","EventCreated":"20180522150801","EventTimestamp":"636625984810407857","EventStatus":"SUCCESS","EventStatusDescription":"","AppName":"Simulator App","ModuleName":"Console","Env":"LOCAL","Division":"destin","Machine":"V02-W10DEV-018","Namespace":"elead.apps.simulator","ConversationId":null,"SessionId":"ab312362-e9cc-41bc-893e-dc9ddb8ec114","ExperienceId":null,"TransactionId":null,"Message":"","UserId":"jfayling"}
]'
```

