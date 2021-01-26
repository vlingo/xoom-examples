package com.vgoairlines.flightplanning.model.flight;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class FlightEntity extends StatefulEntity<FlightState> implements Flight {
  private FlightState state;

  public FlightEntity(final String id) {
    super(String.valueOf(id));
    this.state = FlightState.identifiedBy(id);
  }

  public Completes<FlightState> pool(final Aircraft aircraft) {
    final FlightState stateArg = state.pool(aircraft);
    return apply(stateArg, new AircraftPooled(stateArg), () -> state);
  }

  public Completes<FlightState> schedule(final Schedule schedule) {
    final FlightState stateArg = state.schedule(schedule);
    return apply(stateArg, new FlightScheduled(stateArg), () -> state);
  }

  public Completes<FlightState> reschedule(final Schedule schedule) {
    final FlightState stateArg = state.reschedule(schedule);
    return apply(stateArg, new FlightRescheduled(stateArg), () -> state);
  }

  public Completes<FlightState> cancel() {
    final FlightState stateArg = state.cancel();
    return apply(stateArg, new FlightCanceled(stateArg), () -> state);
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
