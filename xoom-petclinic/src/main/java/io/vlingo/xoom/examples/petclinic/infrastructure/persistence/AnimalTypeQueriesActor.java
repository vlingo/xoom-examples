package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
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
