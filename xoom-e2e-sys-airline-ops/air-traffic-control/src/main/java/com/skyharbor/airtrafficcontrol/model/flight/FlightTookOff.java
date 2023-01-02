// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.model.flight;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class FlightTookOff extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String aircraftId;
  public final String number;
  public final String tailNumber;
  public final String equipment;

  public FlightTookOff(final FlightState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.number = state.number;
    this.aircraftId = state.aircraftId;
    this.tailNumber = state.tailNumber;
    this.equipment = state.equipment;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
