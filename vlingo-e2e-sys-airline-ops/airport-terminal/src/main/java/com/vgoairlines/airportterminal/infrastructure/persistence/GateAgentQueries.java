package com.vgoairlines.airportterminal.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.vgoairlines.airportterminal.infrastructure.GateAgentData;

public interface GateAgentQueries {
  Completes<GateAgentData> gateAgentOf(String id);
  Completes<Collection<GateAgentData>> gateAgents();
}