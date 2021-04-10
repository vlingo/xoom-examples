package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

import io.vlingo.xoom.examples.petclinic.infrastructure.ClientData;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class ClientQueriesActor extends StateStoreQueryActor implements ClientQueries {

  public ClientQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<ClientData> clientOf(String id) {
    return queryStateFor(id, ClientData.class, ClientData.empty());
  }

  @Override
  public Completes<Collection<ClientData>> clients() {
    return streamAllOf(ClientData.class, new ArrayList<>());
  }

}
