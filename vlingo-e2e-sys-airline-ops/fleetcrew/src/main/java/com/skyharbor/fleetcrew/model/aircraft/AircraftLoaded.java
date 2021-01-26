// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.model.aircraft;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

import java.util.UUID;

public final class AircraftLoaded extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final String carrier;

  public AircraftLoaded(final AircraftState state) {
    this.id = state.id;
    this.carrier = state.carrier;
    this.eventId = UUID.randomUUID();
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
