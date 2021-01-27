package com.skyharbor.aircraftmonitoring.model.flight;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Flight {

  Completes<FlightState> departGate(final Aircraft aircraft, final ActualDeparture actualDeparture, final EstimatedArrival estimatedArrival);

  static Completes<FlightState> departGate(final Stage stage, final Aircraft aircraft, final ActualDeparture actualDeparture, final EstimatedArrival estimatedArrival) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Flight _flight = stage.actorFor(Flight.class, Definition.has(FlightEntity.class, Definition.parameters(_address.idString())), _address);
    return _flight.departGate(aircraft, actualDeparture, estimatedArrival);
  }

  Completes<FlightState> reportLocation(final Location location);

  Completes<FlightState> arriveAtGate();

  Completes<FlightState> takeOff();

  Completes<FlightState> land();
}