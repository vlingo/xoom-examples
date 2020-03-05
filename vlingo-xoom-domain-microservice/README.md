# Domain Microservices Example (Vlingo Xoom)

In this example application, you'll be introduced to operational patterns for implementing a microservice with `vlingo-xoom-server` and `micronaut`.

## Architecture

![](https://imgur.com/RtkcJfE.png)

This example has four applications, as shown in the diagram.

- Discovery Service (Spring Cloud Netflix Eureka)
  - Service registry
- API Gateway (Spring Cloud Gateway)
  - Reverse proxy
- Account Service (Vlingo Xoom & Micronaut)
  - Microservice reference example
- Order Service (Vlingo Xoom & Micronaut)
  - Microservice reference example
  
The `discovery-service` and `api-gateway` applications are platform services used to scale and expose functionality of your core domain applications. 

The domain for this reference is contained inside `account-service` and `order-service`. It's also good to point out that `account-service` and `order-service` do not use Spring Boot (microframework) or Netty (embedded servlet container). The `account-service` and `order-service` implement their business logic and distributed process managers using Vlingo (embedded server) and Micronaut (compile-time microframework).

## Process Managers

You can view the management endpoints for process managers at the following locations:

- `http://localhost:9000/account-service/#/account`
- `http://localhost:9000/order-service/#/order`
- `http://localhost:9000/order-service/#/invoice`
- `http://localhost:9000/order-service/#/warehouse`

![](https://imgur.com/uWNA4VY.gif)

A good example of a reactive state transition is the `OrderCreated` state.

When an `Order` is created, it must be connected to an `AccountQuery`. The `AccountQuery` is a query model retrieved from the `AccountContext`. An accountId is provided to the `Order` when the definition is first created by a consumer. 

The `OrderCreated` class is responsible for transitioning the state of an `Order` from `OrderCreated` to `AccountConnected`. The `connectOrder()` method is a command handler that is provided with the account context to check if the `AccountQuery` can be fetched.

Once the `AccountQuery` is fetched from the account microservice, the active shipping address on the account will be copied into the `Order`.  The shipping address is cloned to the order at the time the order is created, which means that the address can only be changed in relation to the order itself.

```java
@Singleton
public class OrderCreated extends OrderState<OrderCreated> {

    private final AccountContext accountContext;
    private final AccountConnected accountConnected;

    public OrderCreated(Provider<AccountContext> accountContext, AccountConnected accountConnected) {
        this.accountContext = accountContext.get();
        this.accountConnected = accountConnected;
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                // Here we define the transition handler from OrderCreated to AccountConnected
                handle(from(this).to(accountConnected).on(Order.class)
                        .then(this::connectAccount)
                        .then(Transition::logResult))
        };
    }

    /**
     * This is the function command for handling the {@link OrderCreated} event. This function will call the
     * account service to retrieve a shipping address and add it to the order.
     *
     * @param order is the definition of the {@link Order} containing the context for this event handler
     * @return the updated {@link Order} definition to be persisted
     */
    private Order connectAccount(Order order) {
        // Retrieve the reactive client request to query the account service via its context
        CompletableFuture<AccountQuery> accountFuture = accountContext.getAccount()
                .query(order.getAccountId())
                .await();

        AccountQuery accountQuery;

        try {
            // This will execute the reactive HTTP request to query the account
            accountQuery = accountFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error getting account query: " + e.getCause().getMessage(), e);
        }

        // The shipping address is then copied from the account query to the order
        order.setShippingAddress(OrderShippingAddress.translateFrom(accountQuery.getAddresses()
                .stream()
                .filter(address -> address.getType().name().equals(OrderShippingAddress.AddressType.SHIPPING.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The account does not have a shipping address"))));

        return order;
    }
```

## Running the Example

To compile this example, clone this repository and execute the following command in your terminal from the project root.

    $ ./gradlew clean assemble docker

This command will build the example projects and corresponding docker images. You must have docker running on your development machine. After the build is completed, run the following commands.

    $ cd ./vlingo-xoom-examples/domain-microservice
    $ docker-compose up -d
    $ docker-compose scale account-service=3

The commands above will spin up the applications listed in the architecture section of this guide and scale the `account-service` to three nodes. You can view the log output by running the following command.

    $ docker-compose logs -f

To verify that the applications routing is being load-balanced between multiple nodes of the `account-service`, you can run the following curl command to communicate with the `account-service` through the `api-gateway`.

    $ while true; do curl http://localhost:9000/account-service/port; echo "\n"; sleep .1; done

The command will run a loop that outputs a request to the `api-proxy` that will route to one of the running nodes of the `account-service` and return the node's port per request. This demonstrates which requests are being routed to which of the running `account-service` nodes.


## Capabilities

This microservice example will demonstrate a basic set of capabilities for operating cloud-native microservices. The `vlingo-xoom` project is meant to provide you with the simplest developer abstractions for building high-performance networked applications. 

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

## Domain and Process Diagrams

Within this application you'll find the recommended implementation patterns for creating a basic microservice for managing a bounded context for a user's account. This is the first tier of multiple examples that will add additional capabilities from vlingo/platform, in addition to DDD patterns and framework abstractions.

## Client Usage Examples
 
Now that the server is running, you can execute the following `curl` commands to interact with the `Account` REST API.
 
### Create Account
 
The following command will create a new `Account`.
 
    curl -X POST http://localhost:9000/account-service/v1/accounts -H "Content-Type: application/json" -d \
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

    curl -X PUT http://localhost:9000/account-service/v1/accounts/1 -H "Content-Type: application/json" -d \
    '{
       "accountNumber": "87654321"
     }' -i

### Get Account

Execute the following command to get an `Account` by its ID.

    curl -X GET -H "Content-Type: application/json" http://localhost:9000/account-service/accounts/1 -i 

### Find Accounts

Execute the following command to get all `Account` entities.

    curl -X GET -H "Content-Type: application/json" http://localhost:9000/account-service/accounts -i 

### Define a new Order

    curl -X POST http://localhost:9000/order-service/v1/orders -H "Content-Type: application/json" -d '{"accountId": 1}' -i
