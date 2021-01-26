package com.skyharbor.fleetcrew.model.aircraft;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class DepartureRecorded extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String gate;
  public final String fleetAgent;

  public DepartureRecorded(final AircraftState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = state.id;
    this.gate = state.gate;
    this.fleetAgent = state.fleetAgent;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
