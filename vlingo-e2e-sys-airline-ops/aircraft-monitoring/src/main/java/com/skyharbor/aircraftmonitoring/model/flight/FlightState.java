// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

import io.vlingo.symbio.store.object.StateObject;

public final class FlightState extends StateObject {

  public final String id;
  public final Status status;
  public final Aircraft aircraft;
  public final ActualDeparture actualDeparture;
  public final EstimatedArrival estimatedArrival;
  public final ActualArrival actualArrival;
  public final Location location;

  public static FlightState identifiedBy(final String id) {
    return new FlightState(id, null, null, null, null, null, null);
  }

  public FlightState (final String id,
                      final Status status,
                      final Aircraft aircraft,
                      final ActualDeparture actualDeparture,
                      final EstimatedArrival estimatedArrival,
                      final ActualArrival actualArrival,
                      final Location location) {
    this.id = id;
    this.status = status;
    this.aircraft = aircraft;
    this.actualDeparture = actualDeparture;
    this.estimatedArrival = estimatedArrival;
    this.actualArrival = actualArrival;
    this.location = location;
  }

  public FlightState departGate(final Aircraft aircraft) {
    return new FlightState(this.id, Status.DEPARTED_GATE, aircraft, ActualDeparture.resolve(), EstimatedArrival.resolve(), this.actualArrival, this.location);
  }

  public FlightState reportLocation(final Location location) {
    return new FlightState(this.id, this.status, this.aircraft, this.actualDeparture, this.estimatedArrival, this.actualArrival, location);
  }

  public FlightState changeStatus(final Status status) {
    return new FlightState(this.id, status, this.aircraft, this.actualDeparture, this.estimatedArrival, this.actualArrival, this.location);
  }

}
