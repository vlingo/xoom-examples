// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.model.aircraft;

import io.vlingo.xoom.symbio.store.object.StateObject;

public final class AircraftState extends StateObject {

  public final String id;
  public final String carrier;
  public final String flightNumber;
  public final String tailNumber;
  public final String gate;
  public final String fleetAgent;

  public static AircraftState identifiedBy(final String id) {
    return new AircraftState(id, null, null, null, null, null);
  }

  public AircraftState (final String id,
                        final String carrier,
                        final String flightNumber,
                        final String tailNumber,
                        final String gate,
                        final String fleetAgent) {
    this.id = id;
    this.carrier = carrier;
    this.flightNumber = flightNumber;
    this.tailNumber = tailNumber;
    this.gate = gate;
    this.fleetAgent = fleetAgent;
  }

  public AircraftState recordArrival(final String carrier, final String flightNumber, final String tailNumber, final String gate) {
    return new AircraftState(this.id,  carrier, flightNumber, tailNumber, gate, this.fleetAgent);
  }

  public AircraftState recordDeparture(final String carrier, final String flightNumber, final String tailNumber, final String gate) {
    return new AircraftState(this.id,  carrier, flightNumber, tailNumber, gate, this.fleetAgent);
  }

  public AircraftState planArrival(final String carrier, final String flightNumber, final String tailNumber) {
    return new AircraftState(this.id, carrier, flightNumber, tailNumber, this.gate, this.fleetAgent);
  }

  public AircraftState reassignGate(final String gate) {
    return new AircraftState(this.id,  this.carrier, this.flightNumber, this.tailNumber, gate, this.fleetAgent);
  }

  public AircraftState recordLoaded(final String carrier) {
    return new AircraftState(this.id,  carrier, this.flightNumber, this.tailNumber, gate, this.fleetAgent);
  }

  public AircraftState recordUnloaded(final String carrier) {
    return new AircraftState(this.id, carrier, this.flightNumber, this.tailNumber, gate, this.fleetAgent);
  }
}
