package com.vgoairlines.airportterminal.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import com.vgoairlines.airportterminal.model.gateagent.GateAgentEntity;
import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import com.vgoairlines.airportterminal.infrastructure.persistence.GateAgentQueriesActor;
import com.vgoairlines.airportterminal.model.gateagent.GateAgent;
import com.vgoairlines.airportterminal.infrastructure.persistence.GateAgentQueries;

import static io.vlingo.http.Method.*;

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