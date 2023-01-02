// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.aircraftmonitoring.infrastructure.exchange.AirTrafficDataMapper.AirTrafficData;
import com.skyharbor.aircraftmonitoring.model.flight.Aircraft;
import com.skyharbor.aircraftmonitoring.model.flight.Flight;
import com.skyharbor.aircraftmonitoring.model.flight.FlightEntity;
import com.skyharbor.aircraftmonitoring.model.flight.Status;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;


public class AirTrafficStatusReceiver implements ExchangeReceiver<Tuple2<Status, AirTrafficData>> {

  private final Stage stage;

  public AirTrafficStatusReceiver(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(final Tuple2<Status, AirTrafficData> message) {
    final Status status = message._1;
    final AirTrafficData data = message._2;

    switch (status) {
      case DEPARTED_GATE:
        Flight.departGate(stage, data.id, Aircraft.of(data.aircraftId, data.tailNumber, data.equipment));
        break;
      case IN_FLIGHT:
        resolveFlight(data.id).andFinallyConsume(Flight::takeOff);
        break;
      case LANDED:
        resolveFlight(data.id).andFinallyConsume(Flight::land);
        break;
      default:
        throw new UnsupportedOperationException("Unable to handle status " + status + "from Air Traffic Control");
    }
  }

  private Completes<Flight> resolveFlight(final String flightId) {
    return stage.actorOf(Flight.class, stage.addressFactory().from(flightId),
            Definition.has(FlightEntity.class, Definition.parameters(flightId)));
  }
}
