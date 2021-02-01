package com.skyharbor.fleetcrew.model.aircraft;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class ArrivalPlanned extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String aircratId;
  public final String carrier;
  public final String flightNumber;
  public final String tailNumber;

  public ArrivalPlanned(final AircraftState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.aircratId = state.id;
    this.carrier = state.carrier;
    this.flightNumber = state.flightNumber;
    this.tailNumber = state.tailNumber;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
