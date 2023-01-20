## Application
Features Service

Application implemented by Impact Zbigniew Bockowski for Capco recruitment process

## Building & Running

### Running from jar
```
gradle build
./gradlew bootRun
```

### Port
8080

### Open Api Documentation (Doesn't work)
Wanted to provide open api docs, but Spring Doc OpenApi setup is broken for Spring Boot 3. Couldn't find solution in reasonable time.
```
/v3/api-docs (json)
/v3/api-docs.yaml (yaml)
```

### SwaggerUI endpoint (Doesn't work)
Wanted to Swagger ui, but Spring Doc OpenApi setup is broken for Spring Boot 3. Couldn't find solution in reasonable time.
```
/swagger-ui.html
```

### Documentation
Application is implemented with usage of latest tech stack, Java 17, Spring Boot 3, Gradle 7.
Following given requirements application let user do request activities, based on assigned role.
Application uses Spring Security configuration at simple form to distinguish user roles and protect access to resources. 
Since time scheduled for task implementation has been limited to couple hours, there are no additional functionalities that could allow manage application users. 
Subsequently, several users were defined in service implementation and stored in memory container. There are 4 users with following details:
1. Admin, username: admin, pass:admin, role: ADMIN
2. User 1, username:user1, pass:user1, role: USER
3. User 2, username:user2, pass:user2, role: USER
4. User 3, username:user3, pass:user3, role: OBSERVER

Application requires authentication with provided Basic Authentication mechanism. 
Unfortunately I was not able to expose Swagger UI or Open Api spec at given time, because of technical issue that looks to be introduced in Spring Boot 3 framework.
There is a short list of available, REST endpoints:

POST /feature  - let user with ADMIN role to create Feature object with 'disabled' flag, defined by default, if other not provided.
Feature object should be provided as JSON body with following properties: 
String name  - feature name
String featureFlag - enumeration ENABLED, DISABLED. Its value is optional.

GET /feature  - let user with role ADMIN load all created features, and user with role USER load all features enabled for him/her, plus all features enabled globally 
Endpoint should return list of Feature objects with structure described above, for previous endpoint with additional id property. For user with role USER his/her identity is extracted from authenticated Principal object.

POST /user/feature  - let user with role ADMIN assign specific feature with given flag.
UserFeature object should be provided as JSON body with following properties:
String userName - user name that plays a role of his/her identifier
UUID featureId - feature id, please note down your features ids for POST calls
String featureFlag - enumeration ENABLED, DISABLED. Indicates if feature should be enabled or disabled for user.

GET /user/feature - let user with ADMIN role load all created user features (for all users)
Endpoint will return set od unique UserFeature objects with structure described for previous endpoint, with additional id property.

Endpoints created for user with ADMIN role, that let him/her to load all data of given type were implemented only to simplify data verification for task reviewer.
User with role OBSERVER is not allowed to do any action.

Application stores data in memory, so that it would not be available after application restart. There is a lot of missing features, that were not implemented because of limited time, among others:
-Already mentioned working Swagger/OpenApi documentation.
-Data validation, please fill in properties in your requests.
-Error handling at any form.
-Data storage at any persistent form and real domain layer.
-Data consistency validation, relations between feature and userFeature objects
-Security layer that should based on more sophisticated solution, delegating user authentication and authorization to valid, external services.
-Fully tested business logic, really because of time limitation. There should be tested all scenarios. A put more impact on integrations tests as that was quickest way to verify application functionalities.
-Service pipeline definition.
-Any containerization setup.
-Postman collections for test purposes.

Finally, implementation may have a bugs, hope not. I didn't have enough time to test it fully. Please understand.  