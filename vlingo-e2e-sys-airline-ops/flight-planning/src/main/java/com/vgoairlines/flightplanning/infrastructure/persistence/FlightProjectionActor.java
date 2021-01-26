package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.Events;
import com.vgoairlines.flightplanning.infrastructure.FlightData;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class FlightProjectionActor extends StateStoreProjectionActor<FlightData> {
  private static final FlightData Empty = FlightData.empty();

  public FlightProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected FlightData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected FlightData merge(FlightData previousData, int previousVersion, FlightData currentData, int currentVersion) {

    if (previousData == null) {
      previousData = currentData;
    }

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case FlightScheduled:
          return FlightData.empty();   // TODO: implement actual merge
        case AircraftPooled:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightRescheduled:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightCanceled:
          return FlightData.empty();   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
