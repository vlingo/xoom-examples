// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

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

  Completes<FlightState> taxi(final String number);

  Completes<FlightState> enterFlightLine(final String number);

  Completes<FlightState> clearForTakeOff(final String number);

  Completes<FlightState> takeOff(final String number);

  Completes<FlightState> clearForLanding(final String number);

  Completes<FlightState> land(final String number);

}