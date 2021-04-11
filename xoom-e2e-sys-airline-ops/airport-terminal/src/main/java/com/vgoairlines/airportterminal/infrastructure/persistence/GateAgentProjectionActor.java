// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.persistence;

import com.vgoairlines.airportterminal.infrastructure.Events;
import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;

public class GateAgentProjectionActor extends StateStoreProjectionActor<GateAgentData> {
  private static final GateAgentData Empty = GateAgentData.empty();

  public GateAgentProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected GateAgentData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected GateAgentData merge(GateAgentData previousData, int previousVersion, GateAgentData currentData, int currentVersion) {

    if (previousData == null) {
      previousData = currentData;
    }

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case GateAgentRegistered:
          return GateAgentData.empty();   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
