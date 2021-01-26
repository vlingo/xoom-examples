package com.vgoairlines.flightplanning.model.flight;

import io.vlingo.symbio.store.object.StateObject;

public final class FlightState extends StateObject {

  public final String id;
  public final Aircraft aircraft;
  public final Schedule schedule;
  public final boolean cancelled;

  public static FlightState identifiedBy(final String id) {
    return new FlightState(id, null, null);
  }

  public FlightState (final String id, final Aircraft aircraft, final Schedule schedule) {
    this(id, aircraft, schedule, false);
  }

  public FlightState (final String id, final Aircraft aircraft, final Schedule schedule, final boolean cancelled) {
    this.id = id;
    this.aircraft = aircraft;
    this.schedule = schedule;
    this.cancelled = cancelled;
  }

  public FlightState pool(final Aircraft aircraft) {
    return new FlightState(this.id, aircraft, this.schedule);
  }

  public FlightState schedule(final Schedule schedule) {
    return new FlightState(this.id, this.aircraft, schedule);
  }

  public FlightState reschedule(final Schedule schedule) {
    return new FlightState(this.id, this.aircraft, schedule);
  }

  public FlightState cancel() {
    return new FlightState(this.id, this.aircraft, this.schedule, true);
  }

}
