package com.vgoairlines.inventory.model.aircraft;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class AircraftConsigned extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final Registration registration;
  public final ManufacturerSpecification manufacturerSpecification;
  public final Carrier carrier;

  public AircraftConsigned(final AircraftState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = state.id;
    this.registration = state.registration;
    this.manufacturerSpecification = state.manufacturerSpecification;
    this.carrier = state.carrier;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
