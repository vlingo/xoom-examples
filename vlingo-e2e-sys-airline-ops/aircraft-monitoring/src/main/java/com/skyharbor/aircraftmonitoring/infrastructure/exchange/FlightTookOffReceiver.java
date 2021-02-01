// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.aircraftmonitoring.model.flight.Aircraft;
import com.skyharbor.aircraftmonitoring.model.flight.Flight;
import com.skyharbor.aircraftmonitoring.model.flight.FlightEntity;
import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import com.skyharbor.airtrafficcontrol.event.FlightTookOff;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.exchange.ExchangeReceiver;

public class FlightTookOffReceiver implements ExchangeReceiver<FlightTookOff> {

  private final Stage stage;

  public FlightTookOffReceiver(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(final FlightTookOff event) {
    final Definition definition =
            Definition.has(FlightEntity.class, Definition.parameters(event.id));

    stage.actorOf(Flight.class, stage.addressFactory().from(event.id), definition).andFinallyConsume(Flight::takeOff);
  }
}
