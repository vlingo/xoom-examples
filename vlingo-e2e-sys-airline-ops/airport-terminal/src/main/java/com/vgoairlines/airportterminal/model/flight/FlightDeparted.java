package com.vgoairlines.airportterminal.model.flight;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class FlightDeparted extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String aircraftId;
  public final String number;
  public final String equipment;
  public final String tailNumber;

  public FlightDeparted(final FlightState state) {
    super(SemanticVersion.from("2.0.0").toValue());
    this.id = state.id;
    this.number = state.number;
    this.aircraftId = state.aircraftId;
    this.equipment = state.equipment.carrier;
    this.tailNumber = state.equipment.tailNumber;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
