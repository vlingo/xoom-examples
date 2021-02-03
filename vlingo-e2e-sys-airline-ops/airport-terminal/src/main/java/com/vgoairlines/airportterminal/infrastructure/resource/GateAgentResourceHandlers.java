// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.annotation.autodispatch.HandlerEntry;

import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import com.vgoairlines.airportterminal.model.gateagent.GateAgentState;
import com.vgoairlines.airportterminal.model.gateagent.GateAgent;
import com.vgoairlines.airportterminal.infrastructure.persistence.GateAgentQueries;
import java.util.Collection;

public class GateAgentResourceHandlers {

  public static final int REGISTER = 0;
  public static final int GATE_AGENTS = 1;
  public static final int ADAPT_STATE = 2;

  public static final HandlerEntry<Three<Completes<GateAgentState>, Stage, GateAgentData>> REGISTER_HANDLER =
          HandlerEntry.of(REGISTER, ($stage, data) -> GateAgent.register($stage, data.name));

  public static final HandlerEntry<Two<GateAgentData, GateAgentState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, GateAgentData::from);

  public static final HandlerEntry<Two<Completes<Collection<GateAgentData>>, GateAgentQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(GATE_AGENTS, GateAgentQueries::gateAgents);

}