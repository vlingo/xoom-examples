// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.persistence;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;
import com.skyharbor.fleetcrew.infrastructure.Events;
import com.skyharbor.fleetcrew.model.aircraft.AircraftState;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;

public class AircraftProjectionActor extends StateStoreProjectionActor<AircraftData> {
  private static final AircraftData Empty = AircraftData.empty();

  public AircraftProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected AircraftData currentDataFor(final Projectable projectable) {
    final AircraftState state = projectable.object();
    return AircraftData.from(state);
  }

  @Override
  protected AircraftData merge(final AircraftData previousData, final int previousVersion, final AircraftData currentData, final int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case DepartureRecorded:
        case ArrivalRecorded:
        case ArrivalPlanned:
          return currentData;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }
    return previousData;
  }
}
