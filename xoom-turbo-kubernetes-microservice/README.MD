# Kubernetes Microservice

In this example application, you'll be introduced to how deploy `vlingo-xoom` + `micronaut` microservices on a `Kubernetes` cluster, exposing them through `Gloo's` API Gateway and using `Helm Chart` to install infrastructure resources.  

**Note that this example is outdated.** It needs an upgrade to the latest version of XOOM/TURBO.

## Project Structure

This project contains two microservices: Order and Stock. The following tree structures are from the project's `./src/main/java` directory of each microservice.

`Order Microservice`

    .
    └── io
        └── examples
            └── infrastructure
            |   └── messaging
            |       ├── ChanelPoolListener.java (JMS Configuration)
            |       └── MessagingClient.jave
            ├── OrderApplication.java
            └── order
                ├── application
                │   ├── OrderApplicationServices.java (Business Logic)
                │   └── RegisterOrder.java (Command)
                ├── data
                │   └── Identity.java
                ├── domain
                │   ├── DomainEvent.java 
                |   ├── DomainEventNotifier.java (Publisher)
                |   ├── Order.java (Aggregate)
                |   ├── OrderWasRegistered.java (Domain Event)
                |   ├── ProductId.java
                │   └── Site.java (Value Object)
                ├── repository
                │   └── OrderRepository.java
                └── endpoint
                    ├── OrderEndpoint.java (Anti-corruption Layer)
                    └── v1
                        └── Orderesource.java

`Stock Microservice`

    .
    └── io
        └── examples
            └── infrastructure
            │   └── messaging
            │       ├── ChanelPoolListener.java (JMS Configuration)
            │       └── DomainEventListener.java
            ├── StockApplication.java
            └── stock
                ├── application
                │   ├── AddItems.java (Command)
                │   ├── OpenStock.java (Command)
                │   ├── StockApplicationServices.java (Business Logic)
                │   └── UnloadItem.java (Command)
                ├── domain
                │   ├── ItemId.java 
                │   ├── Location.java (Value Object)
                │   ├── Stock.java (Aggregate Entity)
                │   └── StorageUnit.java (Value Object)
                └── endpoint
                    ├── StockEndpoint.java (Anti-corruption Layer)
                    └── v1
                        ├── AddItems.java (Command)
                        ├── OpenStock.java (Command)
                        ├── StockResource.java
                        └── UnloadItem.java (Command)

In addition to the tree structure above, in both microservices, you will find `./src/main/tests` for unit tests and `./src/main/java/resources/application.yml` for externalized configuration. In root, the `deployment` folder contains all YAML files related to Kubernetes and Helm Chart.  

## Capabilities

This microservice example will demonstrate a basic set of capabilities for building cloud-native microservices being managed on a single node Kubernetes cluster. The `vlingo-xoom` project is meant to provide you with the simplest developer abstractions for building high-performance networked applications. For building microservices, we've decided to use the JVM microframework, Micronaut. 

### Micronaut

Micronaut is an opinionated JVM microframework that covers a set of capabilities that lend well to multiple application use cases. Micronaut is most popularly used for building microservices. Based on the conventions that were made popular by projects like Spring Boot, Micronaut was built from the ground up to allow native JVM compilation. Micronaut's major leap forward from Spring Boot is its emphasis on compile-time annotation processing instead of using runtime reflection.

In short, Micronaut provides feature parity with Spring Boot but does so at compile time instead of during runtime. Applications have a smaller footprint and can be compiled down into native images that do not need to use the JVM. See [GraalVM Native Image](https://www.graalvm.org/docs/why-graal/#for-microservices-frameworks), for more details.

#### Example Capabilities (Micronaut)

This microservice example demonstrates the following capabilities that are enabled by [Micronaut](http://micronaut.io).

- Dependency Injection and Compile-time Annotations
- Externalized Twelve-factor Configuration ([12factor.net](https://12factor.net/config))
- Embedded Client-Server HTTP Framework with Vlingo Xoom Server


### Vlingo Xoom
 
Vlingo provides a high-performance reactive toolkit for the JVM that is based on the actor model and Domain-Driven Design. Vlingo Xoom brings together the multiple core libraries contained in [vlingo/platform](https://docs.vlingo.io/) for different types of application use cases, such as building reactive microservices. Vlingo focuses on providing an application framework, toolkit, and methodology that enables teams to successfully practice and benefit from DDD.

#### Example Capabilities (Vlingo Xoom)

This microservice example demonstrates the following capabilities that are enabled by [vlingo/platform](http://docs.vlingo.io).

- Docker containerization
- Deployment on Kubernetes
- Helm Chart Packaging
- Reactive Client-Server Framework with [vlingo/http](https://docs.vlingo.io/vlingo-http)
- Domain-Driven Design (DDD)
  - Building an anti-corruption layer
  - Evolving and versioning a REST API
  - Domain Events notification / handling
  - Using versioned endpoint definitions instead of MVC controllers

## Containerization

Two Docker images, one per microservice, were created from the Dockerfiles placed under the root folder. To be built, those images require the microservices executable jar, therefore, the first step is to set up Maven goal to generate artifacts properly. 

Since this application was generated by the [Micronaut CLI](https://docs.micronaut.io/latest/guide/index.html#buildCLI), the Maven Shade Plugin is used, by default, to manipulate the jar building. In order to avoid the invalid signature exception while executing the jar, the following transformers must be added into the pom configuration, specifically in the maven shade plugin section:
    
    <transformer implementation="org.apache.maven.plugins.shade.resource.DontIncludeResourceTransformer">
      <resource>.SF</resource>
    </transformer>
    <transformer implementation="org.apache.maven.plugins.shade.resource.DontIncludeResourceTransformer">
      <resource>.DSA</resource>
    </transformer>
    <transformer implementation="org.apache.maven.plugins.shade.resource.DontIncludeResourceTransformer">
      <resource>.RSA</resource>
    </transformer> 

As of now, the following commands should be executed in both microservices root folder to, respectively, package the application and build the Docker image:  

    $ ./mvn clean package
    $ ./docker build ./ -t vlingo/[microservice-name]:latest

In addition, this application's architecture comprises two Postgres containers and a message-driven middleware (JMS/RabbitMQ) that supports the microservices communication. So far, all the infrastructure resources and microservices can be easily ran using `docker-compose` in the parent root folder:

    $ ./docker-compose up -d

Before the next steps, it's important to check if the microservice containers are up and running by checking the logs: `docker logs [container-name]`. If everything was a success, you should see the following terminal output.

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

### Domain

In a fictious scenario, `Order` and `Stock` represents two bounded contexts. The business starting point is when a `Stock` is opened and items are loaded in it. Once there is a `Stock` with available item quantity and a `Order` is processed, a Domain Event happens and `Stock` context is notified (through a message queue) in order to update the available quantity.

### REST API

- `method=POST, uri=/v1/stocks, to=openStock(OpenStock openStock)`
- `method=PUT, uri=/v1/stocks, to=loadItem(AddItems addItems)`
- `method=GET, uri=/v1/stocks?locationName={}, to=findByLocation(String locationName)`
- `method=POST, uri=/v1/orders, to=registerOrder(RegisterOrder registerOrder)`
- `method=GET, uri=/v1/orders, to=allOrders()`

## Kubernetes

Before deploy the application on Kubernetes, we need to stop docker containers:

    $ ./docker-compose stop

Assuming the Kubernetes is successfully [installed](https://kubernetes.io/docs/tasks/tools/install-kubectl/), the single node cluster should be configured. First, the cluster should be initialized: 

    $ ./sudo kubeadm init --pod-network-cidr=192.168.0.0/16

Map the settings folder:

    $ ./mkdir -p $HOME/.kube
    $ ./sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
    $ ./sudo chown $(id -u):$(id -g) $HOME/.kube/config

Add the network policies (in this case, provided by [Calico](https://docs.projectcalico.org/v2.0/getting-started/kubernetes/)):

    $ ./kubectl apply -f https://docs.projectcalico.org/manifests/calico.yaml   

Remove the taints from the master node preparing it to receive PODs.

    $ ./kubectl taint nodes --all node-role.kubernetes.io/master-

Now the Kubernetes cluster is ready. First, we will deploy the databases and RabbitMQ using the Helm Chart. Simply, the commands below install the charts which were already prepared: 

    $ ./helm install sd-01 ./deployment/helm/stock-database
    $ ./helm install od-01 ./deployment/helm/order-database
    $ ./helm install ms-01 ./deployment/helm/messaging

Finally, deploy the microservices on Kubernetes 

    $ ./kubectl apply -f ./deployment/k8s/microservices

Ensure the PODs are healthy and running 

    $ ./kubectl get pods

## API Gateway

Taking advantage of [Gloo's API Gateway](https://docs.solo.io/gloo/latest/), we're able to expose REST resources having a single entry point to our microservices. Gloo builds on [Envoy Proxy](https://www.envoyproxy.io), a popular service proxy, to add API-level capabilities like transformations, rate limiting, and authentication/authorization and is built to be Kubernetes native (uses CRDs, no extra databases, etc).

The following Gateway setup is made solely through [Gloo command line tool](https://docs.solo.io/gloo/latest/installation/gateway/kubernetes/#install-command-line-tool-cli), so ensure it is installed.

To install the API Gateway on Kubernetes, run the following command:

    $ ./glooctl install gateway

In the previous section, the kubernetes services were already started, so Gloo will detect them and automatically generate [upstreams](https://docs.solo.io/gloo/latest/gloo_routing/hello_world/#verify-the-upstream-for-the-pet-store-application). Whereas the expected status is _accepted_, check it out:

    $ ./glooctl get upstreams
    
   	+---------------------------------------------+------------+----------+--------------------------------+
	|                  UPSTREAM                   |    TYPE    |  STATUS  |            DETAILS             |
	+---------------------------------------------+------------+----------+--------------------------------+
	| default-kubernetes-443                      | Kubernetes | Accepted | svc name:      kubernetes      |
	|                                             |            |          | svc namespace: default         |
	|                                             |            |          | port:          443             |
	|                                             |            |          |                                |
	| default-order-service-8080                  | Kubernetes | Accepted | svc name:      order-service   |
	|                                             |            |          | svc namespace: default         |
	|                                             |            |          | port:          8080            |
	|                                             |            |          |                                |
	| default-stock-service-8081                  | Kubernetes | Accepted | svc name:      stock-service   |
	|                                             |            |          | svc namespace: default         |
	|                                             |            |          | port:          8081            |

Let's add routes for both microservices so Gloo can handle requests mapping the resource path and upstreams.

    $ ./glooctl add route \
	  --path-exact /orders \
	  --dest-name default-order-service-8080 \
	  --prefix-rewrite /v1/orders

    $ ./glooctl add route \
	  --path-exact /stocks \
	  --dest-name default-stock-service-8081 \
	  --prefix-rewrite /v1/stocks
	  
If _Virtual Service_ status is accepted, Gateway API was successfully configured.

	+-----------------+--------------+---------+------+----------+-----------------+----------------------------------+
	| VIRTUAL SERVICE | DISPLAY NAME | DOMAINS | SSL  |  STATUS  | LISTENERPLUGINS |                 ROUTES           |
	+-----------------+--------------+---------+------+----------+-----------------+----------------------------------+
	| default         |              | *       | none | Accepted |                 | /stocks ->                       |
	|                 |              |         |      |          |                 | gloo-system.default-stock-service|
	|		  |		 |         |      |          |                 | -8081				  |
	|                 |              |         |      |          |                 | (upstream)                       |
	|                 |              |         |      |          |                 | /orders ->                       |
	|                 |              |         |      |          |                 | gloo-system.default-order-service|	   |                 |              |         |      |          |                 | -8080			     |
	|                 |              |         |      |          |                 | (upstream)                       | 
	+-----------------+--------------+---------+------+----------+-----------------+----------------------------------+

In this example, we are using the `glooctl` cli tool, but all of this could be done with just `kubectl` and Gloo Custom Resources as well. `glooctl` simplifies and improves the experience with the API Gateway.

## Client Usage Examples
 
Now that all components are running, you can execute the following `curl` commands to interact with the REST API.
 
### Rest API

Rest operations are exposed on Gloo's API Gateway. Therefore, in each request, it's recommended to use a _glooctl_ command in order to resolve the Gloo proxy host transparently. 

    glooctl proxy url

First, it's needed to open a stock on Rest API.

    curl -X POST $(glooctl proxy url)/stocks -H "Content-Type: application/json" -d \
    '{
	    "locationName": "LA"
     }' -i

Then, load the stock in same location for a specific item:

    curl -X PUT $(glooctl proxy url)/stocks -H "Content-Type: application/json" -d \
    '{
        "locationName": "LA",
        "itemId": 2, //Any item id is acceptable
        "quantity": 10
    }' -i

It is possible to check the available quantity by the stock location:

    curl -X GET $(glooctl proxy url)/stocks?locationName=LA

To conclude, an Order can be registered considering the location with available items:

    curl -X POST $(glooctl proxy url)/orders -H "Content-Type: application/json" -d \
    '{
        "productId": 1,
        "quantity": 10,
        "siteName": "LA"
     }' -i

A domain event is sent to `Stock` context and the available quantity is updated. The result is shown by searching here:

    curl -X GET $(glooctl proxy url)/stocks?locationName=LA

Find more information about using `vlingo-xoom` with `Docker`, `Kubernetes` and `Helm Chart` [here](https://docs.vlingo.io/containerization);




