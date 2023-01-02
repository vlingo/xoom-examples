// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure;

import com.vgoairlines.flightplanning.model.flight.FlightState;

import java.util.List;
import java.util.stream.Collectors;

public class FlightData {
  public final String id;
  public final String aircraftId;
  public final ScheduleData schedule;
  public final boolean cancelled;

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
    this.aircraftId = state.aircraftId.id;;
    this.schedule =
            new ScheduleData(
                    new DepartureData(
                            new AirportData(state.schedule.departure.airport.code),
                            state.schedule.departure.plannedFor),
                    new ArrivalData(
                            new AirportData(state.schedule.arrival.airport.code),
                            state.schedule.departure.plannedFor)
            );

    this.cancelled = state.cancelled;
  }

}
