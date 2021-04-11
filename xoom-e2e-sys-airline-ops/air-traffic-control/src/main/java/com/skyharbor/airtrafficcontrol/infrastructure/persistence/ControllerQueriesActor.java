// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerQueriesActor extends StateStoreQueryActor implements ControllerQueries {

  public ControllerQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<ControllerData> controllerOf(String id) {
    return queryStateFor(id, ControllerData.class, ControllerData.empty());
  }

  @Override
  public Completes<Collection<ControllerData>> controllers() {
    return streamAllOf(ControllerData.class, new ArrayList<>());
  }

}
