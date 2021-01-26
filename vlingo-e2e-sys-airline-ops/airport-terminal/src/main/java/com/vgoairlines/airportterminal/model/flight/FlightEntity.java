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

  public Completes<FlightState> openGate(final String number,
                                         final GateAssignment gateAssignment,
                                         final Equipment equipment,
                                         final Schedule schedule) {
    final FlightState stateArg = state.openGate(number, gateAssignment, equipment, schedule);
    return apply(stateArg, new GateOpened(stateArg), () -> state);
  }

  public Completes<FlightState> startBoarding() {
    final FlightState stateArg = state.startBoarding();
    return apply(stateArg, new BoardingStarted(stateArg), () -> state);
  }

  public Completes<FlightState> completeBoarding() {
    final FlightState stateArg = state.completeBoarding();
    return apply(stateArg, new BoardingCompleted(stateArg), () -> state);
  }

  public Completes<FlightState> depart(final LocalDateTime actual) {
    final FlightState stateArg = state.depart(state.schedule.departedOn(actual));
    return apply(stateArg, new FlightDeparted(stateArg), () -> state);
  }

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
