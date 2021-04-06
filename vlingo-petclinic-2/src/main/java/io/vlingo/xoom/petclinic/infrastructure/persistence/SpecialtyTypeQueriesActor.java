package io.vlingo.xoom.petclinic.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import io.vlingo.xoom.petclinic.infrastructure.SpecialtyTypeData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class SpecialtyTypeQueriesActor extends StateStoreQueryActor implements SpecialtyTypeQueries {

  public SpecialtyTypeQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<SpecialtyTypeData> specialtyTypeOf(String id) {
    return queryStateFor(id, SpecialtyTypeData.class, SpecialtyTypeData.empty());
  }

  @Override
  public Completes<Collection<SpecialtyTypeData>> specialtyTypes() {
    return streamAllOf(SpecialtyTypeData.class, new ArrayList<>());
  }

}
