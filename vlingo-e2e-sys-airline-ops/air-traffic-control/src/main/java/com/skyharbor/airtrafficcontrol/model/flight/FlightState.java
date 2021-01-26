package com.skyharbor.airtrafficcontrol.model.flight;

import io.vlingo.symbio.store.object.StateObject;

public final class FlightState extends StateObject {

  public final String id;
  public final String number;
  public final String tailNumber;
  public final String equipment;
  public final FlightStatus status;

  public static FlightState identifiedBy(final String id) {
    return new FlightState(id, null, null, null, null);
  }

  public FlightState (final String id, final String number, final String tailNumber, final String equipment, final FlightStatus status) {
    this.id = id;
    this.number = number;
    this.tailNumber = tailNumber;
    this.equipment = equipment;
    this.status = status;
  }

  public FlightState departGate(final String number, final String tailNumber, final String equipment) {
    return new FlightState(this.id, number, tailNumber, equipment, FlightStatus.DEPARTED_GATE);
  }

  public FlightState tax(final String number) {
    return new FlightState(this.id, number, this.tailNumber, this.equipment, FlightStatus.OUTBOUND_TAXING);
  }

  public FlightState enterFlightLine(final String number) {
    return new FlightState(this.id, number, this.tailNumber, this.equipment, FlightStatus.IN_FLIGHT_LINE);
  }

  public FlightState clearForTakeOff(final String number) {
    return new FlightState(this.id, number, this.tailNumber, this.equipment, FlightStatus.CLEARED_FOR_TAKE_OFF);
  }

  public FlightState takeOff(final String number) {
    return new FlightState(this.id, number, this.tailNumber, this.equipment, FlightStatus.IN_FLIGHT);
  }

  public FlightState clearForLanding(final String number) {
    return new FlightState(this.id, number, this.tailNumber, this.equipment, FlightStatus.CLEARED_FOR_LANDING);
  }

  public FlightState land(final String number) {
    return new FlightState(this.id, number, this.tailNumber, this.equipment, FlightStatus.LANDED);
  }

}
