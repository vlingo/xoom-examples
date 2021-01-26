package com.vgoairlines.airportterminal.model.flight;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class BoardingEnded extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String number;
  public final GateAssignment gateAssignment;
  public final Equipment equipment;
  public final Schedule schedule;

  public BoardingEnded(final FlightState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = state.id;
    this.number = state.number;
    this.gateAssignment = state.gateAssignment;
    this.equipment = state.equipment;
    this.schedule = state.schedule;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
