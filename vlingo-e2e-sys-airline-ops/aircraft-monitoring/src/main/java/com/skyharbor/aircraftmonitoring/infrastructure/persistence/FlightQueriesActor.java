package com.skyharbor.aircraftmonitoring.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;

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
