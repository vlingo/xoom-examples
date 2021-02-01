package com.skyharbor.aircraftmonitoring.model.flight;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Flight {

  Completes<FlightState> departGate(final Aircraft aircraft);

  static Completes<FlightState> departGate(final Stage stage, final String flightId, final Aircraft aircraft) {
    final Address address = stage.addressFactory().uniquePrefixedWith("g-");
    final Flight flight = stage.actorFor(Flight.class, Definition.has(FlightEntity.class, Definition.parameters(flightId)), address);
    return flight.departGate(aircraft);
  }

  Completes<FlightState> reportLocation(final Location location);

  Completes<FlightState> arriveAtGate();

  Completes<FlightState> takeOff();

  Completes<FlightState> land();
}