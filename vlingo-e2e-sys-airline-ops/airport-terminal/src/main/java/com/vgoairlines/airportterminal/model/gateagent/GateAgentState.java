package com.vgoairlines.airportterminal.model.gateagent;

import io.vlingo.symbio.store.object.StateObject;

public final class GateAgentState extends StateObject {

  public final String id;
  public final String name;

  public static GateAgentState identifiedBy(final String id) {
    return new GateAgentState(id, null);
  }

  public GateAgentState (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public GateAgentState register(final String name) {
    return new GateAgentState(this.id, name);
  }

}
