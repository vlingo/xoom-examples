package com.vgoairlines.airportterminal.model.flight;

import io.vlingo.symbio.store.object.StateObject;

public final class FlightState extends StateObject {

  public final String id;
  public final String number;
  public final GateAssignment gateAssignment;
  public final Equipment equipment;
  public final Schedule schedule;
  public final Status status;

  public static FlightState identifiedBy(final String id) {
    return new FlightState(id, null, null, null, null, null);
  }

  public FlightState (final String id,
                      final String number,
                      final GateAssignment gateAssignment,
                      final Equipment equipment,
                      final Schedule schedule,
                      final Status status) {
    this.id = id;
    this.number = number;
    this.gateAssignment = gateAssignment;
    this.equipment = equipment;
    this.schedule = schedule;
    this.status = status;
  }

  public FlightState openGate(final String number,
                              final GateAssignment gateAssignment,
                              final Equipment equipment,
                              final Schedule schedule) {
    return new FlightState(this.id, number, gateAssignment, equipment, schedule, Status.GATE_OPENED);
  }

  public FlightState arrive(final Schedule schedule) {
    return new FlightState(this.id, this.number, this.gateAssignment, this.equipment, schedule, Status.ARRIVED);
  }

  public FlightState startBoarding() {
    return new FlightState(this.id, this.number, this.gateAssignment, this.equipment, this.schedule, Status.BOARDING);
  }

  public FlightState endBoarding() {
    return new FlightState(this.id, this.number, this.gateAssignment, this.equipment, this.schedule, Status.BOARDED);
  }

  public FlightState depart(final Schedule schedule) {
    return new FlightState(this.id, this.number, this.gateAssignment, this.equipment, schedule, Status.DEPARTED);
  }

  public FlightState closeGate() {
    return new FlightState(this.id, this.number, this.gateAssignment, this.equipment, this.schedule, Status.GATE_CLOSED);
  }

}
