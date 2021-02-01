package com.skyharbor.fleetcrew.model.aircraft;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Aircraft {

  static Completes<AircraftState> planArrival(final Stage stage,
                                              final String aircraftId,
                                              final String carrier,
                                              final String flightNumber,
                                              final String tailNumber) {
    final Address address = stage.addressFactory().uniquePrefixedWith("g-");
    final Aircraft aircraft = stage.actorFor(Aircraft.class, Definition.has(AircraftEntity.class, Definition.parameters(aircraftId)), address);
    return aircraft.planArrival(carrier, flightNumber, tailNumber);
  }

  Completes<AircraftState> planArrival(final String carrier, final String flightNumber, final String tailNumber);

  Completes<AircraftState> recordArrival(final String carrier, final String flightNumber, final String tailNumber, final String gate);

  Completes<AircraftState> recordDeparture(final String carrier, final String flightNumber, final String tailNumber, final String gate);

  Completes<AircraftState> reassignGate(final String gate);

  Completes<AircraftState> loadCargo(String carrier);

  Completes<AircraftState> unloadCargo(String carrier);
}