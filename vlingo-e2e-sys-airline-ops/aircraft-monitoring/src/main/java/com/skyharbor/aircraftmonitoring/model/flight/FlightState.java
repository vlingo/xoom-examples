package com.skyharbor.aircraftmonitoring.model.flight;

import io.vlingo.symbio.store.object.StateObject;

public final class FlightState extends StateObject {

  public final String id;
  public final FlightLanded status;
  public final Aircraft aircraft;
  public final ActualDeparture actualDeparture;
  public final EstimatedArrival estimatedArrival;
  public final ActualArrival actualArrival;
  public final Location location;

  public static FlightState identifiedBy(final String id) {
    return new FlightState(id, null, null, null, null, null, null);
  }

  public FlightState (final String id,
                      final FlightLanded flightLanded,
                      final Aircraft aircraft,
                      final ActualDeparture actualDeparture,
                      final EstimatedArrival estimatedArrival,
                      final ActualArrival actualArrival,
                      final Location location) {
    this.id = id;
    this.status = flightLanded;
    this.aircraft = aircraft;
    this.actualDeparture = actualDeparture;
    this.estimatedArrival = estimatedArrival;
    this.actualArrival = actualArrival;
    this.location = location;
  }

  public FlightState departGate(final Aircraft aircraft) {
    return new FlightState(this.id, this.status, aircraft, ActualDeparture.resolve(), EstimatedArrival.resolve(), this.actualArrival, this.location);
  }

  public FlightState reportLocation(final Location location) {
    return new FlightState(this.id, this.status, this.aircraft, this.actualDeparture, this.estimatedArrival, this.actualArrival, location);
  }

  public FlightState changeStatus(final FlightLanded flightLanded) {
    return new FlightState(this.id, flightLanded, this.aircraft, this.actualDeparture, this.estimatedArrival, this.actualArrival, this.location);
  }

}
