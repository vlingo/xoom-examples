// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

import io.vlingo.xoom.symbio.store.object.StateObject;

public final class FlightState extends StateObject {

  public final String id;
  public final String number;
  public final String aircraftId;
  public final GateAssignment gateAssignment;
  public final Equipment equipment;
  public final Schedule schedule;
  public final Status status;

  public static FlightState identifiedBy(final String id) {
    return new FlightState(id, null, null, null, null, null, null);
  }

  public FlightState(final String id,
                     final String number,
                     final String aircraftId,
                     final GateAssignment gateAssignment,
                     final Equipment equipment,
                     final Schedule schedule,
                     final Status status) {
    this.id = id;
    this.number = number;
    this.aircraftId = aircraftId;
    this.gateAssignment = gateAssignment;
    this.equipment = equipment;
    this.schedule = schedule
    ;
    this.status = status;
  }

  public FlightState openGate(final String number,
                              final GateAssignment gateAssignment,
                              final Equipment equipment,
                              final Schedule schedule) {
    return new FlightState(this.id, number, aircraftId, gateAssignment, equipment, schedule, Status.GATE_OPENED);
  }

  public FlightState arrive(final Schedule schedule) {
    return new FlightState(this.id, this.number, this.aircraftId, this.gateAssignment, this.equipment, schedule, Status.ARRIVED);
  }

  public FlightState startBoarding() {
    return new FlightState(this.id, this.number, this.aircraftId, this.gateAssignment, this.equipment, this.schedule, Status.BOARDING);
  }

  public FlightState endBoarding() {
    return new FlightState(this.id, this.number, this.aircraftId, this.gateAssignment, this.equipment, this.schedule, Status.BOARDED);
  }

  public FlightState depart(final Schedule schedule) {
    return new FlightState(this.id, this.number, aircraftId, this.gateAssignment, this.equipment, schedule, Status.DEPARTED);
  }

  public FlightState closeGate() {
    return new FlightState(this.id, this.number, this.aircraftId, this.gateAssignment, this.equipment, this.schedule, Status.GATE_CLOSED);
  }

}
