// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure;

import com.vgoairlines.airportterminal.model.flight.FlightState;

import java.util.List;
import java.util.stream.Collectors;

public class FlightData {
  public final String id;
  public final String aircraftId;
  public final String number;
  public final GateAssignmentData gateAssignment;
  public final EquipmentData equipment;
  public final ScheduleData schedule;

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
    this.gateAssignment = new GateAssignmentData(state.gateAssignment.terminal, state.gateAssignment.number);
    this.equipment = new EquipmentData(state.equipment.carrier, state.equipment.tailNumber);
    this.schedule = new ScheduleData(state.schedule.scheduledDeparture, state.schedule.scheduledArrival,
            new DepartureStatusData(state.schedule.departureStatus.actual, state.schedule.departureStatus.delayedBy));
  }

}
