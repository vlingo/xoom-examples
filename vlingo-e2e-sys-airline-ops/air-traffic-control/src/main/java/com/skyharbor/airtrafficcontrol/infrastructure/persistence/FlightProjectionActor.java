package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import com.skyharbor.airtrafficcontrol.infrastructure.Events;
import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;

import com.skyharbor.airtrafficcontrol.model.flight.FlightState;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

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
  protected FlightData merge(FlightData previousData, int previousVersion, FlightData currentData, int currentVersion) {

    if (previousData == null) {
      previousData = currentData;
    }

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case FlightTookOff:
          return currentData;   // TODO: implement actual merge
        case FlightClearedForLanding:
          return currentData;   // TODO: implement actual merge
        case FlightLanded:
          return currentData;   // TODO: implement actual merge
        case OutboundTaxingInitiated:
          return currentData;   // TODO: implement actual merge
        case FlightClearedForTakeOff:
          return currentData;   // TODO: implement actual merge
        case EnteredFlightLine:
          return currentData;   // TODO: implement actual merge
        case FlightDepartedGate:
          return previousData;   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
