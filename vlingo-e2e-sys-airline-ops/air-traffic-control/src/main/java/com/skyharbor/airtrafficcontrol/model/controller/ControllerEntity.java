package com.skyharbor.airtrafficcontrol.model.controller;

import io.vlingo.common.Completes;

import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class ControllerEntity extends StatefulEntity<ControllerState> implements Controller {
  private ControllerState state;

  public ControllerEntity(final String id) {
    super(String.valueOf(id));
    this.state = ControllerState.identifiedBy(id);
  }

  public Completes<ControllerState> authorize(final String name) {
    final ControllerState stateArg = state.authorize(name);
    return apply(stateArg, new ControllerAuthorized(stateArg), () -> state);
  }

  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the ControllerState
   */
  @Override
  protected void state(final ControllerState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<ControllerState>}
   */
  @Override
  protected Class<ControllerState> stateType() {
    return ControllerState.class;
  }
}
