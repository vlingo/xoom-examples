// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import com.vgoairlines.inventory.infrastructure.AircraftData;

public class AircraftQueriesActor extends StateStoreQueryActor implements AircraftQueries {

  public AircraftQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<AircraftData> aircraftOf(String id) {
    return queryStateFor(id, AircraftData.class, AircraftData.empty());
  }

  @Override
  public Completes<Collection<AircraftData>> aircrafts() {
    return streamAllOf(AircraftData.class, new ArrayList<>());
  }

}
