package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

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
