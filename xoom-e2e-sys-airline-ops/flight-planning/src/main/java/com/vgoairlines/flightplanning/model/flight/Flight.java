// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.flight;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

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