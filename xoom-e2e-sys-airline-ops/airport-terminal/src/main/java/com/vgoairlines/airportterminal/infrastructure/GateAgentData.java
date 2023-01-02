// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure;

import com.vgoairlines.airportterminal.model.gateagent.GateAgentState;

import java.util.List;
import java.util.stream.Collectors;

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
