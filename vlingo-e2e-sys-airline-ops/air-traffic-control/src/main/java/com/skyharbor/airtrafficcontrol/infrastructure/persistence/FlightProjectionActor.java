package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import com.skyharbor.airtrafficcontrol.infrastructure.Events;
import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;

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
        case FlightTookOff:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightClearedForLanding:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightLanded:
          return FlightData.empty();   // TODO: implement actual merge
        case OutboundTaxingInitiated:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightClearedForTakeOff:
          return FlightData.empty();   // TODO: implement actual merge
        case EnteredFlightLine:
          return FlightData.empty();   // TODO: implement actual merge
        case FlightDepartedGate:
          return FlightData.empty();   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
