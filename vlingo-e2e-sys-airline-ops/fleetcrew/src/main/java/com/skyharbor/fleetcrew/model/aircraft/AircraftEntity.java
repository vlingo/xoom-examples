package com.skyharbor.fleetcrew.model.aircraft;

import io.vlingo.common.Completes;

import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class AircraftEntity extends StatefulEntity<AircraftState> implements Aircraft {
  private AircraftState state;

  public AircraftEntity(final String id) {
    super(String.valueOf(id));
    this.state = AircraftState.identifiedBy(id);
  }

  @Override
  public Completes<AircraftState> recordArrival(final String carrier, final String flightNumber, final String tailNumber, final String gate) {
    final AircraftState stateArg = state.recordArrival(carrier, flightNumber, tailNumber, gate);
    return apply(stateArg, new ArrivalRecorded(stateArg), () -> state);
  }

  @Override
  public Completes<AircraftState> recordDeparture(final String carrier, final String flightNumber, final String tailNumber, final String gate) {
    final AircraftState stateArg = state.recordDeparture(carrier, flightNumber, tailNumber, gate);
    return apply(stateArg, new DepartureRecorded(stateArg), () -> state);
  }

  @Override
  public Completes<AircraftState> planArrival(final String carrier, final String flightNumber, final String tailNumber) {
    final AircraftState stateArg = state.planArrival(carrier, flightNumber, tailNumber);
    return apply(stateArg, new ArrivalPlanned(stateArg), () -> state);
  }

  @Override
  public Completes<AircraftState> reassignGate(final String gate) {
    final AircraftState stateArg = state.reassignGate(gate);
    return apply(stateArg, new GateReassigned(stateArg), () -> state);
  }

  @Override
  public Completes<AircraftState> recordLoaded(final String carrier) {
    final AircraftState stateArg = state.recordLoaded(carrier);
    return apply(stateArg, new AircraftLoaded(stateArg), () -> state);
  }

  public Completes<AircraftState> recordUnloaded(final String carrier) {
    final AircraftState stateArg = state.recordUnloaded(carrier);
    return apply(stateArg, new AircraftUnloaded(stateArg), () -> state);
  }
  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the AircraftState
   */
  @Override
  protected void state(final AircraftState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<AircraftState>}
   */
  @Override
  protected Class<AircraftState> stateType() {
    return AircraftState.class;
  }
}
