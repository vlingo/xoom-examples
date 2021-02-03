// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.persistence;

import com.skyharbor.aircraftmonitoring.infrastructure.Events;
import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;

import com.skyharbor.aircraftmonitoring.model.flight.FlightState;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class FlightProjectionActor extends StateStoreProjectionActor<FlightData> {

  public FlightProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected FlightData currentDataFor(final Projectable projectable) {
    final FlightState state = projectable.object();
    return FlightData.from(state);
  }

  @Override
  protected FlightData merge(FlightData previousData, int previousVersion, FlightData currentData, int currentVersion) {

    if (previousData == null) {
      previousData = currentData;
    }

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case Landed:
          return FlightData.empty();   // TODO: implement actual merge
        case DepartedGate:
          return FlightData.empty();   // TODO: implement actual merge
        case LocationReported:
          return FlightData.empty();   // TODO: implement actual merge
        case InFlight:
          return FlightData.empty();   // TODO: implement actual merge
        case ArrivedAtGate:
          return FlightData.empty();   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
