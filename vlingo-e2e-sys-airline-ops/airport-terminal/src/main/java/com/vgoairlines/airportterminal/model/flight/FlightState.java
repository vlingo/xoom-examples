package com.vgoairlines.airportterminal.model.flight;

import io.vlingo.symbio.store.object.StateObject;

public final class FlightState extends StateObject {

  public final String id;
  public final String number;
  public final GateAssignment gateAssignment;
  public final Equipment equipment;
  public final Schedule schedule;

  public static FlightState identifiedBy(final String id) {
    return new FlightState(id, null, null, null, null);
  }

  public FlightState (final String id,
                      final String number,
                      final GateAssignment gateAssignment,
                      final Equipment equipment,
                      final Schedule schedule) {
    this.id = id;
    this.number = number;
    this.gateAssignment = gateAssignment;
    this.equipment = equipment;
    this.schedule = schedule;
  }

  public FlightState openGate(final String number,
                              final GateAssignment gateAssignment,
                              final Equipment equipment,
                              final Schedule schedule) {
    return new FlightState(this.id, number, gateAssignment, equipment, schedule);
  }

  public FlightState startBoarding() {
    //TODO: Define how to update the state
    return this;
  }

  public FlightState completeBoarding() {
    //TODO: Define how to update the state
    return this;
  }

  public FlightState depart(final Schedule schedule) {
    return new FlightState(this.id, this.number, this.gateAssignment, this.equipment, schedule);
  }

  public FlightState closeGate() {
    //TODO: Define how to update the state
    return this;
  }

}
