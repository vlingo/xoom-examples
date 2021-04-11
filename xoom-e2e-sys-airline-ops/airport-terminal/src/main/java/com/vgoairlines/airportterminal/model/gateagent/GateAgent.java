// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.gateagent;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface GateAgent {

  Completes<GateAgentState> register(final String name);

  static Completes<GateAgentState> register(final Stage stage, final String name) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final GateAgent _gateAgent = stage.actorFor(GateAgent.class, Definition.has(GateAgentEntity.class, Definition.parameters(_address.idString())), _address);
    return _gateAgent.register(name);
  }

}