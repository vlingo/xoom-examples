// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.model.flight.Flight;
import com.vgoairlines.airportterminal.event.FlightDeparted;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

public class FlightDepartedReceiver implements ExchangeReceiver<FlightDeparted> {

  private final Stage stage;

  public FlightDepartedReceiver(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(final FlightDeparted event) {
    Flight.departFromGate(stage, event.aircraftId, event.number, event.tailNumber, event.equipment);
  }
}