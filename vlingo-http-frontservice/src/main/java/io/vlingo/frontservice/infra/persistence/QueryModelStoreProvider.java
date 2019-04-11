// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import java.util.Collection;

import io.vlingo.actors.Stage;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.State;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateStore.Dispatcher;
import io.vlingo.symbio.store.state.StateStore.DispatcherControl;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class QueryModelStoreProvider {
  private static QueryModelStoreProvider instance;

  public final Queries queries;
  public final StateStore store;

  public static QueryModelStoreProvider instance() {
    return instance;
  }

  public static QueryModelStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    if (instance != null) return instance;

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(UserData.class, new UserDataStateAdapter());
    stateAdapterProvider.registerAdapter(ProfileData.class, new ProfileDataStateAdapter());
    new EntryAdapterProvider(stage.world()); // future

    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public <S extends State<?>, E extends Entry<?>> void dispatch(final String dispatchId, final S state, final Collection<E> entries) { }
    };

    final StateStore store = stage.actorFor(StateStore.class, InMemoryStateStoreActor.class, noop);

    final Queries queries = stage.actorFor(Queries.class, QueriesActor.class, store);

    instance = new QueryModelStoreProvider(registry, store, queries);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStoreProvider(final StatefulTypeRegistry registry, final StateStore store, final Queries queries) {
    this.store = store;
    this.queries = queries;

    registry
      .register(new Info(store, UserData.class, UserData.class.getSimpleName()))
      .register(new Info(store, ProfileData.class, ProfileData.class.getSimpleName()));
  }
}
