package com.skyharbor.fleetcrew.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;

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
