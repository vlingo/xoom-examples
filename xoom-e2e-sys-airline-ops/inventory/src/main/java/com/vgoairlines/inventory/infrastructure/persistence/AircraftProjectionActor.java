// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.infrastructure.persistence;

import com.vgoairlines.inventory.infrastructure.AircraftData;
import com.vgoairlines.inventory.infrastructure.Events;
import com.vgoairlines.inventory.model.aircraft.AircraftState;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class AircraftProjectionActor extends StateStoreProjectionActor<AircraftData> {

  public AircraftProjectionActor() {
    super(ComponentRegistry.withType(QueryModelStateStoreProvider.class).store);
  }

  @Override
  protected AircraftData currentDataFor(final Projectable projectable) {
    final AircraftState aircraftState = projectable.object();
    return AircraftData.from(aircraftState);
  }

  @Override
  protected AircraftData merge(final AircraftData previousData,
                               final int previousVersion,
                               final AircraftData currentData,
                               final int currentVersion) {

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case AircraftConsigned:
          return currentData;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
