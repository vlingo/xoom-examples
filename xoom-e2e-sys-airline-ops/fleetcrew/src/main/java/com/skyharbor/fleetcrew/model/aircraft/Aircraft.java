// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.model.aircraft;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

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