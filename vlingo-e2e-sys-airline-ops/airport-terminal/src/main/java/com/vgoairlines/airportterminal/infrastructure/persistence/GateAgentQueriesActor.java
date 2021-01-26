package com.vgoairlines.airportterminal.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import com.vgoairlines.airportterminal.infrastructure.GateAgentData;

public class GateAgentQueriesActor extends StateStoreQueryActor implements GateAgentQueries {

  public GateAgentQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<GateAgentData> gateAgentOf(String id) {
    return queryStateFor(id, GateAgentData.class, GateAgentData.empty());
  }

  @Override
  public Completes<Collection<GateAgentData>> gateAgents() {
    return streamAllOf(GateAgentData.class, new ArrayList<>());
  }

}
