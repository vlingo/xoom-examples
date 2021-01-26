package com.vgoairlines.airportterminal.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.vgoairlines.airportterminal.model.gateagent.GateAgentState;

public class GateAgentData {
  public final String id;
  public final String name;

  public static GateAgentData from(final GateAgentState state) {
    return new GateAgentData(state);
  }

  public static List<GateAgentData> from(final List<GateAgentState> states) {
    return states.stream().map(GateAgentData::from).collect(Collectors.toList());
  }

  public static GateAgentData empty() {
    return new GateAgentData(GateAgentState.identifiedBy(null));
  }

  private GateAgentData (final GateAgentState state) {
    this.id = state.id;
    this.name = state.name;
  }

}
