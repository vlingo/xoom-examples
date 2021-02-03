// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class AircraftConsigned extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String aircraftId;
  public final String model;
  public final String serialNumber;
  public final String tailNumber;

  public AircraftConsigned(final AircraftState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.aircraftId = state.id;
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
