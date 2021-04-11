// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.aircraft;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class AircraftPooled extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String model;
  public final String serialNumber;
  public final String tailNumber;

  public AircraftPooled(final AircraftState state) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = state.id;
    this.model = state.denomination.model;
    this.serialNumber = state.denomination.serialNumber;
    this.tailNumber = state.denomination.tailNumber;
    this.eventId = UUID.randomUUID();
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
