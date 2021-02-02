// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;

public class FlightQueriesActor extends StateStoreQueryActor implements FlightQueries {

  public FlightQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<FlightData> flightOf(String id) {
    return queryStateFor(id, FlightData.class, FlightData.empty());
  }

  @Override
  public Completes<Collection<FlightData>> flights() {
    return streamAllOf(FlightData.class, new ArrayList<>());
  }

}
