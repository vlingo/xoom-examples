// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure;

import com.skyharbor.airtrafficcontrol.model.flight.FlightState;

import java.util.List;
import java.util.stream.Collectors;

public class FlightData {
  public final String id;
  public final String aircraftId;
  public final String number;
  public final String tailNumber;
  public final String equipment;
  public final String status;

  public static FlightData from(final FlightState state) {
    return new FlightData(state);
  }

  public static List<FlightData> from(final List<FlightState> states) {
    return states.stream().map(FlightData::from).collect(Collectors.toList());
  }

  public static FlightData empty() {
    return new FlightData(FlightState.identifiedBy(null));
  }

  private FlightData (final FlightState state) {
    this.id = state.id;
    this.number = state.number;
    this.aircraftId = state.aircraftId;
    this.tailNumber = state.tailNumber;
    this.equipment = state.equipment;
    this.status = state.status.name();
  }

}
