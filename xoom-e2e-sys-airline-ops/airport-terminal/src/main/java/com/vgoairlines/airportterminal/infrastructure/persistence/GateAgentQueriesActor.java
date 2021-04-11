// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.persistence;

import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

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
