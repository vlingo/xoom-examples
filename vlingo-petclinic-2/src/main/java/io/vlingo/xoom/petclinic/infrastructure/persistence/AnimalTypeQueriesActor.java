package io.vlingo.xoom.petclinic.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import io.vlingo.xoom.petclinic.infrastructure.AnimalTypeData;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class AnimalTypeQueriesActor extends StateStoreQueryActor implements AnimalTypeQueries {

  public AnimalTypeQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<AnimalTypeData> animalTypeOf(String id) {
    return queryStateFor(id, AnimalTypeData.class, AnimalTypeData.empty());
  }

  @Override
  public Completes<Collection<AnimalTypeData>> animalTypes() {
    return streamAllOf(AnimalTypeData.class, new ArrayList<>());
  }

}
