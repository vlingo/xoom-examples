// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure;

import com.skyharbor.fleetcrew.model.aircraft.AircraftState;

import java.util.List;
import java.util.stream.Collectors;

public class AircraftData {
  public final String aircraftId;
  public final String carrier;
  public final String flightNumber;
  public final String tailNumber;
  public final String gate;
  public final String fleetAgent;

  public static AircraftData from(final AircraftState state) {
    return new AircraftData(state);
  }

  public static List<AircraftData> from(final List<AircraftState> states) {
    return states.stream().map(AircraftData::from).collect(Collectors.toList());
  }

  public static AircraftData empty() {
    return new AircraftData(AircraftState.identifiedBy(null));
  }

  private AircraftData (final AircraftState state) {
    this.aircraftId = state.id;
    this.carrier = state.carrier;
    this.flightNumber = state.flightNumber;
    this.tailNumber = state.tailNumber;
    this.gate = state.gate;
    this.fleetAgent = state.fleetAgent;
  }

}
