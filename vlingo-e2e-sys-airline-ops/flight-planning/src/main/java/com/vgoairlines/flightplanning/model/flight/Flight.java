package com.vgoairlines.flightplanning.model.flight;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Flight {

  Completes<FlightState> schedule(final AircraftId aircraftId, final Schedule schedule);

  static Completes<FlightState> schedule(final Stage stage, final AircraftId aircraftId, final Schedule schedule) {
    final Address address = stage.addressFactory().uniquePrefixedWith("g-");
    final Flight flight = stage.actorFor(Flight.class, Definition.has(FlightEntity.class, Definition.parameters(address.idString())), address);
    return flight.schedule(aircraftId, schedule);
  }

  Completes<FlightState> reschedule(final Schedule schedule);

  Completes<FlightState> cancel();

}