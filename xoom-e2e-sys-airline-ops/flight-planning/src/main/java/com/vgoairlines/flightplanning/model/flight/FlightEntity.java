// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.flight;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

public final class FlightEntity extends StatefulEntity<FlightState> implements Flight {
  private FlightState state;

  public FlightEntity(final String id) {
    super(String.valueOf(id));
    this.state = FlightState.identifiedBy(id);
  }

  public Completes<FlightState> schedule(final AircraftId aircraftId, final Schedule schedule) {
    final FlightState stateArg = state.schedule(aircraftId, schedule);
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
