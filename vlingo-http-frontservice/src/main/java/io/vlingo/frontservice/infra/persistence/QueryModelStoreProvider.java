// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.store.state.StateStore.DispatcherControl;
import io.vlingo.symbio.store.state.TextStateStore;
import io.vlingo.symbio.store.state.TextStateStore.TextDispatcher;
import io.vlingo.symbio.store.state.inmemory.InMemoryTextStateStoreActor;

public class QueryModelStoreProvider {
  private static QueryModelStoreProvider instance;

  public final Queries queries;
  public final TextStateStore store;

  public static QueryModelStoreProvider instance() {
    return instance;
  }

  public static QueryModelStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    if (instance != null) return instance;

    final TextDispatcher noop = new TextDispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatchText(final String dispatchId, final TextState state) { }
    };

    final TextStateStore store =
            stage.actorFor(
                    Definition.has(InMemoryTextStateStoreActor.class, Definition.parameters(noop)),
                    TextStateStore.class);

    final Queries queries =
            stage.actorFor(
                    Definition.has(QueriesActor.class, Definition.parameters(store)),
                    Queries.class);

    instance = new QueryModelStoreProvider(registry, store, queries);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStoreProvider(final StatefulTypeRegistry registry, final TextStateStore store, final Queries queries) {
    this.store = store;
    this.queries = queries;

    registry
      .register(new Info(store, UserData.class, UserData.class.getSimpleName(), new UserDataStateAdapter()))
      .register(new Info(store, ProfileData.class, ProfileData.class.getSimpleName(), new ProfileDataStateAdapter()));
  }
}
