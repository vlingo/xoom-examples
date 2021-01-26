package com.vgoairlines.airportterminal.model.gateagent;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface GateAgent {

  Completes<GateAgentState> register(final String name);

  static Completes<GateAgentState> register(final Stage stage, final String name) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final GateAgent _gateAgent = stage.actorFor(GateAgent.class, Definition.has(GateAgentEntity.class, Definition.parameters(_address.idString())), _address);
    return _gateAgent.register(name);
  }

}