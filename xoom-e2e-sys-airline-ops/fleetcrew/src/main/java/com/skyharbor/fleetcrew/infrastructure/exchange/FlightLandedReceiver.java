// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import com.skyharbor.fleetcrew.infrastructure.persistence.LogisticsResolver;
import com.skyharbor.fleetcrew.model.aircraft.Aircraft;
import com.skyharbor.fleetcrew.model.aircraft.AircraftEntity;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

class FlightLandedReceiver implements ExchangeReceiver<FlightLanded> {

  private final Stage stage;

  public FlightLandedReceiver(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(final FlightLanded event) {
    final Address address =
            stage.addressFactory().from(event.aircraftId);

    final Definition definition =
            Definition.has(AircraftEntity.class, Definition.parameters(event.aircraftId));

    stage.actorOf(Aircraft.class, address, definition).andFinallyConsume(aircraft ->{
      final String carrier = LogisticsResolver.availableCarrier();
      final String gate = LogisticsResolver.availableGate();
      aircraft.recordArrival(carrier, event.number, event.tailNumber, gate);
    });
  }
}
