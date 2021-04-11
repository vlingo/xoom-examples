// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.persistence;

import com.vgoairlines.airportterminal.infrastructure.Events;
import com.vgoairlines.airportterminal.infrastructure.FlightData;
import com.vgoairlines.airportterminal.model.flight.FlightState;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;

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
  protected FlightData merge(final FlightData previousData,
                             final int previousVersion,
                             final FlightData currentData,
                             final int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case BoardingStarted:
        case GateClosed:
        case FlightArrived:
        case FlightDeparted:
        case BoardingEnded:
        case GateOpened:
          return currentData;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
