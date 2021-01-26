package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;

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
