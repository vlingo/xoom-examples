// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.model.flight;

import io.vlingo.common.Completes;

import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class FlightEntity extends StatefulEntity<FlightState> implements Flight {
  private FlightState state;

  public FlightEntity(final String id) {
    super(String.valueOf(id));
    this.state = FlightState.identifiedBy(id);
  }

  public Completes<FlightState> departGate(final String aircraftId, final String number, final String tailNumber, final String equipment) {
    logger().info("Changing flight status to DEPARTED_GATE");
    final FlightState stateArg = state.departGate(aircraftId, number, tailNumber, equipment);
    return apply(stateArg, new FlightDepartedGate(stateArg), () -> state);
  }

  public Completes<FlightState> taxi(final String number) {
    final FlightState stateArg = state.tax(number);
    return apply(stateArg, new OutboundTaxingInitiated(stateArg), () -> state);
  }

  public Completes<FlightState> enterFlightLine(final String number) {
    final FlightState stateArg = state.enterFlightLine(number);
    return apply(stateArg, new EnteredFlightLine(stateArg), () -> state);
  }

  public Completes<FlightState> clearForTakeOff(final String number) {
    final FlightState stateArg = state.clearForTakeOff(number);
    return apply(stateArg, new FlightClearedForTakeOff(stateArg), () -> state);
  }

  public Completes<FlightState> takeOff(final String number) {
    final FlightState stateArg = state.takeOff(number);
    return apply(stateArg, new FlightTookOff(stateArg), () -> state);
  }

  public Completes<FlightState> clearForLanding(final String number) {
    final FlightState stateArg = state.clearForLanding(number);
    return apply(stateArg, new FlightClearedForTakeOff(stateArg), () -> state);
  }

  public Completes<FlightState> land(final String number) {
    final FlightState stateArg = state.land(number);
    return apply(stateArg, new FlightLanded(stateArg), () -> state);
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
