// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.Events;
import com.vgoairlines.flightplanning.infrastructure.FlightData;
import com.vgoairlines.flightplanning.model.flight.FlightState;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class FlightProjectionActor extends StateStoreProjectionActor<FlightData> {

  public FlightProjectionActor() {
    super(ComponentRegistry.withType(QueryModelStateStoreProvider.class).store);
  }

  @Override
  protected FlightData currentDataFor(final Projectable projectable) {
    final FlightState state = projectable.object();
    return FlightData.from(state);
  }

  @Override
  protected FlightData merge(final FlightData previousData, final int previousVersion, final FlightData currentData, final int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case FlightScheduled:
        case AircraftPooled:
        case FlightRescheduled:
        case FlightCanceled:
          return currentData;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }
    return previousData;
  }
}
