package com.skyharbor.airtrafficcontrol.model.controller;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class ControllerAuthorized extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String name;

  public ControllerAuthorized(final ControllerState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = state.id;
    this.name = state.name;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
