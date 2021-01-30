package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class AircraftConsigned extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String model;
  public final String serialNumber;
  public final String tailNumber;

  public AircraftConsigned(final AircraftState state) {
    super(SemanticVersion.from("4.0.0").toValue());
    this.id = state.id;
    this.model = state.manufacturerSpecification.model;
    this.serialNumber = state.manufacturerSpecification.serialNumber;
    this.tailNumber = state.registration.tailNumber;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
