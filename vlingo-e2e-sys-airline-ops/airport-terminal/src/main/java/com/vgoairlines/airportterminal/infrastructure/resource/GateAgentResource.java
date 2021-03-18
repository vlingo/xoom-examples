// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.resource;

import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import com.vgoairlines.airportterminal.infrastructure.persistence.GateAgentQueries;
import com.vgoairlines.airportterminal.infrastructure.persistence.GateAgentQueriesActor;
import com.vgoairlines.airportterminal.model.gateagent.GateAgent;
import com.vgoairlines.airportterminal.model.gateagent.GateAgentEntity;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.xoom.annotation.autodispatch.*;

import static io.vlingo.http.Method.GET;
import static io.vlingo.http.Method.POST;

@AutoDispatch(path="/gate-agents", handlers=GateAgentResourceHandlers.class)
@Queries(protocol = GateAgentQueries.class, actor = GateAgentQueriesActor.class)
@Model(protocol = GateAgent.class, actor = GateAgentEntity.class, data = GateAgentData.class)
public interface GateAgentResource {

  @Route(method = POST, path = "/", handler = GateAgentResourceHandlers.REGISTER)
  @ResponseAdapter(handler = GateAgentResourceHandlers.ADAPT_STATE)
  Completes<Response> register(@Body final GateAgentData data);

  @Route(method = GET, handler = GateAgentResourceHandlers.GATE_AGENTS)
  Completes<Response> gateAgents();

}