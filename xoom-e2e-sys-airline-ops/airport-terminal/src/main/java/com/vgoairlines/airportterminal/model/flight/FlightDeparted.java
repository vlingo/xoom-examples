// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class FlightDeparted extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String aircraftId;
  public final String number;
  public final String equipment;
  public final String tailNumber;

  public FlightDeparted(final FlightState state) {
    super(SemanticVersion.from("1.0.0").toValue());
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
