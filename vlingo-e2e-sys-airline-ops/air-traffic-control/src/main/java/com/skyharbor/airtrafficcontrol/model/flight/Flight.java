package com.skyharbor.airtrafficcontrol.model.flight;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Flight {

  Completes<FlightState> departGate(final String aircraftId, final String number, final String tailNumber, final String equipment);

  static Completes<FlightState> departGate(final Stage stage, final String aircraftId, final String number, final String tailNumber, final String equipment) {
    final Address address = stage.addressFactory().uniquePrefixedWith("g-");
    final Flight flight = stage.actorFor(Flight.class, Definition.has(FlightEntity.class, Definition.parameters(address.idString())), address);
    return flight.departGate(aircraftId, number, tailNumber, equipment);
  }

  Completes<FlightState> tax(final String number);

  Completes<FlightState> enterFlightLine(final String number);

  Completes<FlightState> clearForTakeOff(final String number);

  Completes<FlightState> takeOff(final String number);

  Completes<FlightState> clearForLanding(final String number);

  Completes<FlightState> land(final String number);

}