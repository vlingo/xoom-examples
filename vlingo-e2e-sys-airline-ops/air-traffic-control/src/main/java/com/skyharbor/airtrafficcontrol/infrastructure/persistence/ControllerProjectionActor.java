package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import com.skyharbor.airtrafficcontrol.infrastructure.Events;

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class ControllerProjectionActor extends StateStoreProjectionActor<ControllerData> {
  private static final ControllerData Empty = ControllerData.empty();

  public ControllerProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected ControllerData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected ControllerData merge(ControllerData previousData, int previousVersion, ControllerData currentData, int currentVersion) {

    if (previousData == null) {
      previousData = currentData;
    }

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case ControllerAuthorized:
          return ControllerData.empty();   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
