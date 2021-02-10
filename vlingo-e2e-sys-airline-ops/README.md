# Aircraft-Airport Ops System Example

The "Aircraft-Airport-Ops System" example implements a set of Reactive Microservices that interact through a choregraphed Event-Driven Architecture.

- All microservices designed with VLINGO/XOOM Starter
- Six _Bounded Contexts_
- Choreographed process flows
- Domain Models Diagram
- Big-Picture Event-Driven Model

System collaboration is managed through type-safe, asynchronous, event-driven, and message-based communication provided by VLINGO/XOOM, VLINGO/LATTICE Exchange, and VLINGO/SCHEMATA. Respectively, these platform components provide a REST request and messaging auto-dispatch to the domain model, message exchange mechanism, and a Schema Registry where the DDD _Published Language_ specifications are available.

![Context Map of Ops System](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineAirportOpsContextMap.png)

![Domain Models of Bounded Contexts](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineOpsDomainModel.png)

The [Context Map](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineAirportOpsContextMap.png), [Domain Models](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineOpsDomainModel.png), and [Big-Picture Event Storming Summary](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineAirportOpsBigPicture.png) diagrams show different views of the Aircraft-Airport-Ops with six _Bounded Contexts_ and its choreographed process flows.

![Big-Picture Event Storming Summary](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineAirportOpsBigPicture.png)

## Services

Under the root folder, there is a specific folder for each service. The steps to build it and run it are described below:
 1. Install and run `RabbitMQ` message broker
 2. Keep the [VLINGO/SCHEMATA](https://github.com/vlingo/vlingo-schemata) up and running during the service compilation and packaging
 3. Under each project folder, the database and message exchange settings have to be update on `vlingo-xoom` properties
 4. Run the following maven build goal on a terminal window then execute the built `jar` passing a port number:

```
$ mvn clean install
...
$ java -jar target/[jar-name].jar [port-number]
```

__Note that the services need to be built in the following order: Inventory, Flight Planning, Fleet Crew, Airport Terminal, Air Traffic Control and Aircraft Monitoring__.  

The `install` goal will also publish the schema files and pull the required ones for receiving Type-safe messages from other _Bounded Contexts._

## Exchanging messages between _Bounded Contexts_

Let's get started by requesting an Aircraft Consignment on the `Inventory` service. Consequently, an `AircraftConsigned` event will be emitted and consumed by `Flight Planning` service.   

The next command uses `curl` to perform a `POST` request for consigning an aircraft:

```
$ curl -i -X POST -H "Content-Type: application/json" -d '{"registration":{"tailNumber":"VGO7777"}, "manufacturerSpecification" : { "manufacturer":"EXECUTIVE JETS", "model": "J17", "serialNumber": "10987"}, "carrier": {"name": "INTERCONTINENTAL", "type": "AIRLINE"} }' http://hostname:port/aircrafts
``` 

The `Flight Planning` domain model will react to the `AircraftConsigned` event, receiving and handling it on [AircraftConsignedReceiver](https://github.com/vlingo/vlingo-examples/blob/0a7f9f207e67ad8a34c01bf5a78cf046360a9c1c/vlingo-e2e-sys-airline-ops/flight-planning/src/main/java/com/vgoairlines/flightplanning/infrastructure/exchange/AircraftConsignedReceiver.java) class:

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

## More information

This example demonstrates how VLINGO/LATTICE Exchange and VLINGO/SCHEMATA collaborates efficiently making easier to establish a domain model - message handling connection based on a type-safe _Published Language._ Find a comprehensive documentation on VLINGO components [here](http://docs.vlingo.io).
