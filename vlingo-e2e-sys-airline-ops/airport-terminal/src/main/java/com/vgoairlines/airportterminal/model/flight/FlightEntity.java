// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

import java.time.LocalDateTime;

public final class FlightEntity extends StatefulEntity<FlightState> implements Flight {
  private FlightState state;

  public FlightEntity(final String id) {
    super(String.valueOf(id));
    this.state = FlightState.identifiedBy(id);
  }

  @Override
  public Completes<FlightState> openGate(final String number,
                                         final GateAssignment gateAssignment,
                                         final Equipment equipment,
                                         final Schedule schedule) {
    final FlightState stateArg = state.openGate(number, gateAssignment, equipment, schedule);
    return apply(stateArg, new GateOpened(stateArg), () -> state);
  }


  @Override
  public Completes<FlightState> arrive(final LocalDateTime arrivedOn) {
    final FlightState stateArg = state.arrive(state.schedule.arrivedOn(arrivedOn));
    return apply(stateArg, new FlightArrived(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> startBoarding() {
    final FlightState stateArg = state.startBoarding();
    return apply(stateArg, new BoardingStarted(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> endBoarding() {
    final FlightState stateArg = state.endBoarding();
    return apply(stateArg, new BoardingEnded(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> depart(final LocalDateTime actual) {
    final FlightState stateArg = state.depart(state.schedule.departedOn(actual));
    return apply(stateArg, new FlightDeparted(stateArg), () -> state);
  }

  @Override
  public Completes<FlightState> closeGate() {
    final FlightState stateArg = state.closeGate();
    return apply(stateArg, new GateClosed(stateArg), () -> state);
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
