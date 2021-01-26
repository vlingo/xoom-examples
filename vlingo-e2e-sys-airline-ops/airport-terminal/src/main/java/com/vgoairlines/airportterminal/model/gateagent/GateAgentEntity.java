package com.vgoairlines.airportterminal.model.gateagent;

import io.vlingo.common.Completes;

import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class GateAgentEntity extends StatefulEntity<GateAgentState> implements GateAgent {
  private GateAgentState state;

  public GateAgentEntity(final String id) {
    super(String.valueOf(id));
    this.state = GateAgentState.identifiedBy(id);
  }

  public Completes<GateAgentState> register(final String name) {
    final GateAgentState stateArg = state.register(name);
    return apply(stateArg, new GateAgentRegistered(stateArg), () -> state);
  }

  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the GateAgentState
   */
  @Override
  protected void state(final GateAgentState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<GateAgentState>}
   */
  @Override
  protected Class<GateAgentState> stateType() {
    return GateAgentState.class;
  }
}
