// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.gateagent;

import io.vlingo.xoom.symbio.store.object.StateObject;

public final class GateAgentState extends StateObject {

  public final String id;
  public final String name;

  public static GateAgentState identifiedBy(final String id) {
    return new GateAgentState(id, null);
  }

  public GateAgentState (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public GateAgentState register(final String name) {
    return new GateAgentState(this.id, name);
  }

}
