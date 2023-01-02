// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.persistence;

import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import io.vlingo.xoom.common.Completes;

import java.util.Collection;

public interface GateAgentQueries {
  Completes<GateAgentData> gateAgentOf(String id);
  Completes<Collection<GateAgentData>> gateAgents();
}