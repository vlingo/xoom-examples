# Aircraft-Airport Ops System Example

The "Aircraft-Airport-Ops System" example implements a set of Reactive Microservices that interact through a choregraphed Event-Driven Architecture.

- All microservices designed with VLINGO XOOM Designer
- Six _Bounded Contexts_
- Choreographed process flows
- Domain Models Diagram
- Big-Picture Event-Driven Model

System collaboration is managed through type-safe, asynchronous, event-driven, and message-based communication provided by XOOM Turbo, XOOM Lattice Exchange, and XOOM Schemata. Respectively, these platform components provide a REST request and messaging auto-dispatch to the domain model, message exchange mechanism, and a Schema Registry where the DDD _Published Language_ specifications are available.

![Context Map of Ops System](https://github.com/vlingo/xoom-examples/blob/master/xoom-e2e-sys-airline-ops/docs/AirlineAirportOpsContextMap.png)

![Domain Models of Bounded Contexts](https://github.com/vlingo/xoom-examples/blob/master/xoom-e2e-sys-airline-ops/docs/AirlineOpsDomainModel.png)

The [Context Map](https://github.com/vlingo/xoom-examples/blob/master/xoom-e2e-sys-airline-ops/docs/AirlineAirportOpsContextMap.png), [Domain Models](https://github.com/vlingo/xoom-examples/blob/master/xoom-e2e-sys-airline-ops/docs/AirlineOpsDomainModel.png), and [Big-Picture Event Storming Summary](https://github.com/vlingo/xoom-examples/blob/master/xoom-e2e-sys-airline-ops/docs/AirlineAirportOpsBigPicture.png) diagrams show different views of the Aircraft-Airport-Ops with six _Bounded Contexts_ and its choreographed process flows.

![Big-Picture Event Storming Summary](https://github.com/vlingo/xoom-examples/blob/master/xoom-e2e-sys-airline-ops/docs/AirlineAirportOpsBigPicture.png)

## Setup

Before start the Aircraft Ops services, the following infrastructure resources need to be up and running: 

- Postgres DB
- Rabbit MQ
- Schemata Service

It is highly recommended that these resources be configured and run through the [Docker Compose file](https://github.com/vlingo/xoom-examples/blob/dbcfd3fc395a92fc8413879defe85fd14f3e065e/xoom-e2e-sys-airline-ops/docker-compose.yml) present in this folder. Ensure that *18787*, *5672* and *9019* ports are not in use then run the containerized resources: 

```
$ docker-compose up -d
```

## Services

To start each "Aircraft-Airport-Ops" services, first we need to package it executing the following Maven goal:

```
$ mvn clean package
```

Run each service through executable `jar` passing a port number:

```
$ java -jar [service-folder-name]/target/[jar-name].jar -Dport=[port-number]
```

## Exchanging messages between _Bounded Contexts_

Let's get started by requesting an Aircraft Consignment on the `Inventory` service. Consequently, an `AircraftConsigned` event will be emitted and consumed by `Flight Planning` service.   

The next command uses `curl` to perform a `POST` request for consigning an aircraft:

```
$ curl -i -X POST -H "Content-Type: application/json" -d '{"registration":{"tailNumber":"VGO7777"}, "manufacturerSpecification" : { "manufacturer":"EXECUTIVE JETS", "model": "J17", "serialNumber": "10987"}, "carrier": {"name": "INTERCONTINENTAL", "type": "AIRLINE"} }' http://hostname:port/aircrafts
``` 

The `Flight Planning` domain model will react to the `AircraftConsigned` event, receiving and handling it on [AircraftConsignedReceiver](https://github.com/vlingo/xoom-examples/blob/0a7f9f207e67ad8a34c01bf5a78cf046360a9c1c/xoom-e2e-sys-airline-ops/flight-planning/src/main/java/com/vgoairlines/flightplanning/infrastructure/exchange/AircraftConsignedReceiver.java) class:

```
class AircraftConsignedReceiver implements ExchangeReceiver<AircraftConsigned> {

  ...
  
 @Override
  public void receive(final AircraftConsigned aircraftConsigned) {
    final Denomination denomination =
            Denomination.from(aircraftConsigned.model, aircraftConsigned.serialNumber,
                    aircraftConsigned.tailNumber);

    Aircraft.pool(stage, aircraftConsigned.aircraftId, denomination);
  }
}

```
  
When the aircraft is pooled, an info message is shown on the `Flight Planning` logs:

```
INFO com.vgoairlines.flightplanning.model.aircraft.AircraftEntity - Aircraft with id 123 has been successfully pooled
```
  
Now, let's use the aircraft id to begin the departure / arrival / landing process which involves `Fleet Crew` and `Air Traffic Control` service. The following request plans the aircraft arrival on `Fleet Creew`: 
      
```
$ curl -i -X POST -H "Content-Type: application/json" -d '{"aircraftId": "123", "carrier": "EXECUTIVE JETS", "flightNumber": "3881", "tailNumber": "2021"}' http://hostname:port/fleetcrew/aircrafts/
```

The `Air Traffic Control` is responsible for keep tracking of every flight phase on an airport. In that sense, the requests above are meant to respectively report the departure and landing: 

```
$ curl -i -X POST -H "Content-Type: application/json" -d '{"aircraftId": "123", "number": "3881", "tailNumber": "2021",  "equipment": "RPC ENGINES" }' http://hostname:port/flights
$ curl -i -X PATCH -H "Content-Type: application/json" -d '{"number": "3881", "status": "LANDED"}' http://hostname:port/flights/123
```

The completion of a landing is represented by `FlightLanded` event on `Air Traffic Control`. That occurrence needs to be notified to the `Fleet Crew` service so that the arrival is recorded.`Here's how it's implemented:

```
class FlightLandedReceiver implements ExchangeReceiver<FlightLanded> {
  
  ...

  @Override
  public void receive(final FlightLanded event) {
    final Address address =
            stage.addressFactory().from(event.aircraftId);

    final Definition definition =
            Definition.has(AircraftEntity.class, Definition.parameters(event.aircraftId));

    stage.actorOf(Aircraft.class, address, definition).andFinallyConsume(aircraft ->{
      final String carrier = LogisticsResolver.availableCarrier();
      final String gate = LogisticsResolver.availableGate();
      aircraft.recordArrival(carrier, event.number, event.tailNumber, gate);
    });
  }
}
``` 

The arrival record can be verified when the message below is logged:

 ```
 INFO com.skyharbor.fleetcrew.model.aircraft.AircraftEntity - Arrival has been successfully recorded. Flight number: 3881  
 ```

 ## How to run UI

 1. Go to frontend folder

 ```
 cd frontend
 ```

 2. Install dependencies
 ```
 npm install
 ```

3. Run Dev Server

```
npm run dev
```

## More information

This example demonstrates how XOOM Lattice Exchange and XOOM Schemata collaborates efficiently making easier to establish a domain model - message handling connection based on a type-safe _Published Language._ Find a comprehensive documentation on VLINGO XOOM components [here](http://docs.vlingo.io).
