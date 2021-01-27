package com.skyharbor.aircraftmonitoring.model.flight;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class InFlight extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String aircraft;
  public final String actualDeparture;
  public final String estimatedArrival;
  public final String actualArrival;

  public InFlight(final FlightState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = state.id;
    this.aircraft = state.aircraft;
    this.actualDeparture = state.actualDeparture;
    this.estimatedArrival = state.estimatedArrival;
    this.actualArrival = state.actualArrival;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
