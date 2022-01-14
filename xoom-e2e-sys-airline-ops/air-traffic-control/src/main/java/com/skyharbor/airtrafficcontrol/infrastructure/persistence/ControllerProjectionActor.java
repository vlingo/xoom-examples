// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import com.skyharbor.airtrafficcontrol.infrastructure.Events;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class ControllerProjectionActor extends StateStoreProjectionActor<ControllerData> {
  private static final ControllerData Empty = ControllerData.empty();

  public ControllerProjectionActor() {
    super(ComponentRegistry.withType(QueryModelStateStoreProvider.class).store);
  }

  @Override
  protected ControllerData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected ControllerData merge(ControllerData previousData, int previousVersion, ControllerData currentData, int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case ControllerAuthorized:
          return currentData;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }
    return previousData;
  }
}
