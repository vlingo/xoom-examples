# vlingo-e2e-sys-airline-ops

The "Aircraft-Airport-Ops" example implements a set of services that communicates with each other through VLINGO/LATTICE Exchange and VLINGO/SCHEMATA. 
Respectively, those components provide a message broker implementation for publishing Domain Events and a Schema Registry where the published language specifications are available.            

The [Model](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineOpsDomainModel.png) and [Event Storming](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-e2e-sys-airline-ops/docs/AirlineAirportOpsBigPicture.png) diagram show the Aircraft-Airport-Ops with six Bounded Contexts and its choreographed process flows.

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

The `install` goal will also publish the schema files and pull the required ones for receiving Type-safe messages from other bounded contexts.

## Exchanging messages between Bounded Contexts 

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
  


