// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class FlightEntity extends StatefulEntity<FlightState> implements Flight {
  private FlightState state;

  public FlightEntity(final String id) {
    super(String.valueOf(id));
    this.state = FlightState.identifiedBy(id);
  }

  @Override
  public Completes<FlightState> departGate(final Aircraft aircraft) {
    logger().info("Changing flight status to DEPARTED_GATE");
    final FlightState stateArg = state.departGate(aircraft);
    return apply(stateArg, new DepartedGate(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> reportLocation(final Location location) {
    final FlightState stateArg = state.reportLocation(location);
    return apply(stateArg, new LocationReported(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> arriveAtGate() {
    final FlightState stateArg = state.changeStatus(Status.ARRIVED_AT_GATE);
    return apply(stateArg, new ArrivedAtGate(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> takeOff() {
    logger().info("Changing flight status to IN_FLIGHT");
    final FlightState stateArg = state.changeStatus(Status.IN_FLIGHT);
    return apply(stateArg, new InFlight(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> land() {
    logger().info("Changing flight status to LANDED");
    final FlightState stateArg = state.changeStatus(Status.LANDED);
    return apply(stateArg, new Landed(stateArg), () -> state);
  }

  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the FlightState
   */
  @Override
  protected void state(final FlightState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<FlightState>}
   */
  @Override
  protected Class<FlightState> stateType() {
    return FlightState.class;
  }
}
