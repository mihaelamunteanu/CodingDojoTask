Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

Make initial sources production-grade. For achieving this steps were taken for the following aspects: 
* logging
* testing (unit tests and integration with embedded and regular postgres db)
* exception handling
* code refactoring (e.g. for having a Service layer for business logic, externalize the apiKey not to expose it, exception hierarchy)
* validation (in this case minimal validation was added)
* java doc and comments where considered
* usage of additional libraries (see [pom.xml](/pom.xml))
* usage of lombok for beans boiler plate

### Delivered the code

A file local.properties containing appId=<yourWeatherApiKey> should be added so that the code can run.

>**DO NOT create a Pull Request with your solution** 


#### Tests
##### Integration Tests
A smoke (end-to-end) integration test is done for a test database (not embedded). 
The properties files for test database is configured so that the tables are dropped and recreated every time.
By default this integration test is excluded (to be seen in pom.xml) so that it does not it does not put time pressure for each rebuild/install. 
It can be tested separately with **mvn -Dtest="com.assignment.spring.integration.*Test" test** 

##### Unit tests
Unit tests tager
* Controller Tests 
* Service Tests
* Exceptions are treated


### Assumptions + additions + modifications
* For the context of this application it was considered the single request by City was enough. (http://api.openweathermap.org/data/2.5/weather?q={city}&APPID={appid}, detailed weather was added).
* The properties for the production database (main properties file) were modified so that the database is not dropped and recreated with each application restart. 

* Path was added for identifying and version
* RequestMapping annotation was replaced with GetMapping

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
