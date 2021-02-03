// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.model.aircraft;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class ArrivalPlanned extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String aircraftId;
  public final String carrier;
  public final String flightNumber;
  public final String tailNumber;

  public ArrivalPlanned(final AircraftState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.aircraftId = state.id;
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
