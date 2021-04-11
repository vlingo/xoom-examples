// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

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