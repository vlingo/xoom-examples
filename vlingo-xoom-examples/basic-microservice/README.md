# Vlingo + Micronaut Microservice Example

In this example application, you'll be introduced to the basic patterns for implementing a microservice with `vlingo-xoom-server` and `micronaut`.

## Project Structure

The following tree structure is from the project's `./src/main/java` directory. Here we can see the domain entities, the endpoint definition, and the repositories for data management. 

`Account Microservice`

    .
    └── io
        └── examples
            ├── AccountApplication.java
            └── account
                ├── data
                │   ├── Auditable.java
                │   └── BaseEntity.java
                ├── domain
                │   ├── Account.java (Aggregate Entity)
                │   ├── AccountService.java (Business Logic)
                │   ├── Address.java
                │   └── CreditCard.java
                ├── endpoint
                │   ├── AccountEndpoint.java (Anti-corruption Layer)
                │   └── v1
                │       └── AccountResource.java
                └── repository
                    └── AccountRepository.java (Entity Persistence)

In addition to the tree structure above, you will find `./src/main/tests` for unit tests and `./src/main/java/resources/application.yml` for externalized configuration.

## Capabilities

This microservice example will demonstrate a basic set of capabilities for building cloud-native microservices. The `vlingo-xoom` project is meant to provide you with the simplest developer abstractions for building high-performance networked applications. For building microservices, we've decided to use the JVM microframework, Micronaut. 

### Micronaut

Micronaut is an opinionated JVM microframework that covers a set of capabilities that lend well to multiple application use cases. Micronaut is most popularly used for building microservices. Based on the conventions that were made popular by projects like Spring Boot, Micronaut was built from the ground up to allow native JVM compilation. Micronaut's major leap forward from Spring Boot is its emphasis on compile-time annotation processing instead of using runtime reflection.

In short, Micronaut provides feature parity with Spring Boot but does so at compile time instead of during runtime. Applications have a smaller footprint and can be compiled down into native images that do not need to use the JVM. See [GraalVM Native Image](https://www.graalvm.org/docs/why-graal/#for-microservices-frameworks), for more details.

#### Example Capabilities (Micronaut)

This microservice example demonstrates the following capabilities that are enabled by [Micronaut](http://micronaut.io).

- Dependency Injection and Compile-time Annotations
- Externalized Twelve-factor Configuration ([12factor.net](https://12factor.net/config))
- Repository-based Persistence with JPA/Hibernate
- Embedded Client-Server HTTP Framework with Vlingo Xoom Server
- Embedded In-Memory Database with H2
- Unit Testing with Micronaut's HTTP Client

### Vlingo Xoom
 
Vlingo provides a high-performance reactive toolkit for the JVM that is based on the actor model and Domain-Driven Design. Vlingo Xoom brings together the multiple core libraries contained in [vlingo/platform](https://docs.vlingo.io/) for different types of application use cases, such as building reactive microservices. Vlingo focuses on providing an application framework, toolkit, and methodology that enables teams to successfully practice and benefit from DDD.

#### Example Capabilities (Vlingo Xoom)

This microservice example demonstrates the following capabilities that are enabled by [vlingo/platform](http://docs.vlingo.io).

- Reactive Client-Server Framework with [vlingo/http](https://docs.vlingo.io/vlingo-http)
- Domain-Driven Design (DDD)
  - Example microservice starter kit for a sample domain
  - Building an anti-corruption layer
  - Evolving and versioning a REST API
  - Using versioned endpoint definitions instead of MVC controllers

## Account Domain

Within this application you'll find the recommended implementation patterns for creating a basic microservice for managing a bounded context for a user's account. This is the first tier of multiple examples that will add additional capabilities from vlingo/platform, in addition to DDD patterns and framework abstractions.

This microservice is also an example of an anemic domain model, that is, it only covers basic CRUD operations on `Account` entities. This was done intentionally to magnify the capabilities listed in the previous section with minimal code. The examples that follow this one will iterate from this anemic domain model and provide a rich command-driven interface.   

### REST API

- `method=GET, uri=/v1/accounts/{id}, to=getAccount(Long id)`
- `method=PUT, uri=/v1/accounts/{id}, to=updateAccount(Long id, Account body)`
- `method=DELETE, uri=/v1/accounts/{id}, to=deleteAccount(Long id)`
- `method=GET, uri=/v1/accounts, to=findAccounts()`
- `method=POST, uri=/v1/accounts, to=createAccount(Account body)`

## Running the Example

To compile this example, clone this repository and execute the following command in your terminal.

    $ ./gradlew assemble

After you successfully compile the application, execute the next command to run the microservice.

    $ ./gradlew run

If everything was a success, you should see the following terminal output.

    2019-11-06 09:46:43 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
    2019-11-06 09:46:43 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
    2019-11-06 09:46:43 [main] INFO  org.hibernate.Version - HHH000412: Hibernate Core {5.4.7.Final}
    2019-11-06 09:46:43 [main] INFO  o.h.annotations.common.Version - HCANN000001: Hibernate Commons Annotations {5.1.0.Final}
    2019-11-06 09:46:43 [main] INFO  org.hibernate.dialect.Dialect - HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
    2019-11-06 09:46:44 [main] INFO  io.vlingo.VlingoScene - New scene created: __defaultStage
    2019-11-06 09:46:44 [pool-3-thread-4] INFO  i.v.a.Logger - ServerRequestResponseChannelActor: OPENING PORT: 8080
    2019-11-06 09:46:44 [pool-3-thread-3] INFO  i.v.a.Logger - Server vlingo-http-server is listening on port: 8080 started in 47 ms
    2019-11-06 09:46:44 [pool-3-thread-3] INFO  i.v.a.Logger - Resource: Account Endpoint Resource v1.1
    2019-11-06 09:46:44 [pool-3-thread-3] INFO  i.v.a.Logger - Action: id=0, method=GET, uri=/v1/accounts/{id}, to=dynamic1(Long id)
    2019-11-06 09:46:44 [pool-3-thread-3] INFO  i.v.a.Logger - Action: id=1, method=PUT, uri=/v1/accounts/{id}, to=dynamic2(Long id)
    2019-11-06 09:46:44 [pool-3-thread-3] INFO  i.v.a.Logger - Action: id=2, method=DELETE, uri=/v1/accounts/{id}, to=dynamic3(Long id)
    2019-11-06 09:46:44 [pool-3-thread-3] INFO  i.v.a.Logger - Action: id=3, method=GET, uri=/v1/accounts, to=dynamic4()
    2019-11-06 09:46:44 [pool-3-thread-3] INFO  i.v.a.Logger - Action: id=4, method=POST, uri=/v1/accounts, to=dynamic5()
    2019-11-06 09:46:44 [main] INFO  io.vlingo.VlingoServer - 
                ░▒░▒░      _ _                    
                ░▒░▒░     | (_)                   
     ▒░▒░▒   ░▒░▒░  __   _| |_ _ __   __ _  ___   
     ▒░▒░▒   ░▒░▒░  \ \ / / | | '_ \ / _` |/ _ \
         ▒░▒░▒       \ V /| | | | | | (_| | (_) |
         ░▒░▒░        \_/ |_|_|_| |_|\__, |\___/
                                      __/ | Xoom v0.1.0
                                     |___/
    2019-11-06 09:46:44 [main] INFO  io.vlingo.VlingoServer - Started embedded Vlingo Xoom server at http://localhost:8080
    2019-11-06 09:46:44 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 1846ms. Server Running: http://localhost:8080
 
## Client Usage Examples
 
Now that the server is running, you can execute the following `curl` commands to interact with the `Account` REST API.
 
### Create Account
 
The following command will create a new `Account`.
 
    curl -X POST http://localhost:8080/v1/accounts -H "Content-Type: application/json" -d \
    '{
       "accountNumber": "12345678",
       "creditCards": [
         {
           "type": "VISA",
           "number": "1234567881234567"
         }
       ],
       "addresses": [
         {
           "street1": "101 Third St",
           "street2": "Suite 3",
           "state": "CA",
           "city": "Palo Alto",
           "country": "USA",
           "zipCode": 94301,
           "type": "SHIPPING"
         }
       ]
     }' -i

### Update Account

Execute the following command to update the account number on the `Account` we just created.

    curl -X PUT http://localhost:8080/v1/accounts/1 -H "Content-Type: application/json" -d \
    '{
       "accountNumber": "87654321"
     }' -i

### Get Account

Execute the following command to get an `Account` by its ID.

    curl -X GET -H "Content-Type: application/json" http://localhost:8080/accounts/1 -i 

### Find Accounts

Execute the following command to get all `Account` entities.

    curl -X GET -H "Content-Type: application/json" http://localhost:8080/accounts -i 
