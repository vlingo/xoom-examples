package com.vgoairlines.inventory.infrastructure.persistence;

import com.vgoairlines.inventory.infrastructure.AircraftData;
import com.vgoairlines.inventory.infrastructure.Events;
import com.vgoairlines.inventory.model.aircraft.AircraftState;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class AircraftProjectionActor extends StateStoreProjectionActor<AircraftData> {

  public AircraftProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected AircraftData currentDataFor(final Projectable projectable) {
    final AircraftState aircraftState = projectable.object();
    return AircraftData.from(aircraftState);
  }

  @Override
  protected AircraftData merge(AircraftData previousData, int previousVersion, AircraftData currentData, int currentVersion) {

    if (previousData == null) {
      previousData = currentData;
    }

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case AircraftConsigned:
          return currentData;   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
