// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.AircraftData;
import com.vgoairlines.flightplanning.infrastructure.Events;
import com.vgoairlines.flightplanning.model.aircraft.AircraftState;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class AircraftProjectionActor extends StateStoreProjectionActor<AircraftData> {

  public AircraftProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected AircraftData currentDataFor(final Projectable projectable) {
    final AircraftState state = projectable.object();
    return AircraftData.from(state);
  }

  @Override
  protected AircraftData merge(AircraftData previousData, int previousVersion, AircraftData currentData, int currentVersion) {

    if (previousData == null) {
      previousData = currentData;
    }

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case AircraftPooled:
          return previousData;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
