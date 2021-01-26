package com.vgoairlines.flightplanning.model.flight;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Flight {

  Completes<FlightState> pool(final Aircraft aircraft);

  Completes<FlightState> schedule(final Schedule schedule);

  static Completes<FlightState> schedule(final Stage stage, final Schedule schedule) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Flight _flight = stage.actorFor(Flight.class, Definition.has(FlightEntity.class, Definition.parameters(_address.idString())), _address);
    return _flight.schedule(schedule);
  }

  Completes<FlightState> reschedule(final Schedule schedule);

  Completes<FlightState> cancel();

}