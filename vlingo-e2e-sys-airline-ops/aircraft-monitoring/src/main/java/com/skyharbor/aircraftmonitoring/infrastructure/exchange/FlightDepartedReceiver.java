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
import com.skyharbor.airtrafficcontrol.event.FlightDepartedGate;
import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.exchange.ExchangeReceiver;

public class FlightDepartedReceiver implements ExchangeReceiver<FlightDepartedGate> {

  private final Stage stage;

  public FlightDepartedReceiver(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(final FlightDepartedGate event) {
    Flight.departGate(stage, event.id, Aircraft.of(event.aircraftId, event.tailNumber, event.equipment));
  }
}
