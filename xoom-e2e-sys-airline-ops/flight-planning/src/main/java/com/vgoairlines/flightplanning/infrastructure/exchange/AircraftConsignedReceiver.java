// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.exchange;

import com.vgoairlines.flightplanning.model.aircraft.Aircraft;
import com.vgoairlines.flightplanning.model.aircraft.Denomination;
import com.vgoairlines.inventory.event.AircraftConsigned;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

class AircraftConsignedReceiver implements ExchangeReceiver<AircraftConsigned> {

  private final Stage stage;

  public AircraftConsignedReceiver(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(final AircraftConsigned aircraftConsigned) {
    final Denomination denomination =
            Denomination.from(aircraftConsigned.model, aircraftConsigned.serialNumber,
                    aircraftConsigned.tailNumber);

    Aircraft.pool(stage, aircraftConsigned.aircraftId, denomination);
  }
}
