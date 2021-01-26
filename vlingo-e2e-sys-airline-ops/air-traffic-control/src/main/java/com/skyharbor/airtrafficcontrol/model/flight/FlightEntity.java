package com.skyharbor.airtrafficcontrol.model.flight;

import io.vlingo.common.Completes;

import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class FlightEntity extends StatefulEntity<FlightState> implements Flight {
  private FlightState state;

  public FlightEntity(final String id) {
    super(String.valueOf(id));
    this.state = FlightState.identifiedBy(id);
  }

  public Completes<FlightState> departGate(final String number, final String tailNumber, final String equipment) {
    final FlightState stateArg = state.departGate(number, tailNumber, equipment);
    return apply(stateArg, new FlightDepartedGate(stateArg), () -> state);
  }

  public Completes<FlightState> tax(final String number) {
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
