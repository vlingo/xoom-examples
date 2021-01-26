package com.vgoairlines.airportterminal.infrastructure.persistence;

import com.vgoairlines.airportterminal.infrastructure.Events;
import com.vgoairlines.airportterminal.infrastructure.FlightData;

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
        case BoardingStarted:
          return FlightData.empty();   // TODO: implement actual merge
        case GateClosed:
          return FlightData.empty();   // TODO: implement actual merge
        case GateOpened:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightArrived:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightDeparted:
          return FlightData.empty();   // TODO: implement actual merge
        case BoardingEnded:
          return FlightData.empty();   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
