package com.skyharbor.airtrafficcontrol.model.flight;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class FlightTookOff extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String aircraftId;
  public final String number;
  public final String tailNumber;
  public final String equipment;

  public FlightTookOff(final FlightState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.number = state.number;
    this.aircraftId = state.aircraftId;
    this.tailNumber = state.tailNumber;
    this.equipment = state.equipment;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
