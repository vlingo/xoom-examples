// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.infra.persistence;

import java.util.Arrays;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.examples.frontservice.data.ProfileData;
import io.vlingo.xoom.examples.frontservice.data.UserData;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class QueryModelStoreProvider {
  private static QueryModelStoreProvider instance;

  public final Queries queries;
  public final StateStore store;

  public static QueryModelStoreProvider instance() {
    return instance;
  }

  @SuppressWarnings("rawtypes")
  public static QueryModelStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    if (instance != null) return instance;

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(UserData.class, new UserDataStateAdapter());
    stateAdapterProvider.registerAdapter(ProfileData.class, new ProfileDataStateAdapter());
    new EntryAdapterProvider(stage.world()); // future

    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    final StateStore store = stage.actorFor(StateStore.class, InMemoryStateStoreActor.class, Arrays.asList(noop));

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
