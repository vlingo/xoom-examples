// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import com.skyharbor.airtrafficcontrol.infrastructure.Events;
import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;
import com.skyharbor.airtrafficcontrol.model.flight.FlightState;
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
        case FlightTookOff:
        case FlightClearedForLanding:
        case FlightLanded:
        case OutboundTaxingInitiated:
        case FlightClearedForTakeOff:
        case EnteredFlightLine:
        case FlightDepartedGate:
          return currentData;   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
