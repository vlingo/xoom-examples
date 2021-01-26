package com.skyharbor.fleetcrew.model.aircraft;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Aircraft {

  Completes<AircraftState> recordArrival(final String carrier, final String flightNumber, final String tailNumber, final String gate);

  Completes<AircraftState> recordDeparture(final String carrier, final String flightNumber, final String tailNumber, final String gate);

  Completes<AircraftState> planArrival(final String carrier, final String flightNumber, final String tailNumber);

  Completes<AircraftState> reassignGate(final String gate);

  static Completes<AircraftState> planArrival(final Stage stage, final String carrier, final String flightNumber, final String tailNumber) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Aircraft _aircraft = stage.actorFor(Aircraft.class, Definition.has(AircraftEntity.class, Definition.parameters(_address.idString())), _address);
    return _aircraft.planArrival(carrier, flightNumber, tailNumber);
  }

  Completes<AircraftState> recordLoaded(String carrier);
}