// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

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
