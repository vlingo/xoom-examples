package com.vgoairlines.flightplanning.model.aircraft;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class AircraftPooled extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String model;
  public final String serialNumber;
  public final String tailNumber;

  public AircraftPooled(final AircraftState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = state.id;
    this.model = state.model;
    this.serialNumber = state.serialNumber;
    this.tailNumber = state.tailNumber;
    this.eventId = UUID.randomUUID();
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
