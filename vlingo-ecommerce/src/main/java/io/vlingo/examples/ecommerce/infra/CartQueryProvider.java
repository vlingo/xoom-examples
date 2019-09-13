// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.examples.ecommerce.infra;

import io.vlingo.actors.Stage;
import io.vlingo.examples.ecommerce.model.CartQuery;
import io.vlingo.examples.ecommerce.model.CartQueryActor;
import io.vlingo.examples.ecommerce.model.CartUserSummaryData;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.state.StateStore;

public class CartQueryProvider {
  private static CartQueryProvider instance;

  public final CartQuery cartQuery;
  public final StateStore store;

  public static CartQueryProvider instance() {
    return instance;
  }

  @SuppressWarnings("rawtypes")
  public static CartQueryProvider using(final Stage stage,
                                        final StatefulTypeRegistry registry,
                                        final StateStore stateStoreProtocol) {
    if (instance != null) return instance;

    registerStateAdapter(stage);

    final CartQuery queries = stage.actorFor(CartQuery.class, CartQueryActor.class, stateStoreProtocol);

    instance = new CartQueryProvider(registry, stateStoreProtocol, queries);

    return instance;
  }

  private static void registerStateAdapter(Stage stage) {
    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(CartUserSummaryData.class, new CartStateAdapter());
    new EntryAdapterProvider(stage.world()); // future?
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CartQueryProvider(final StatefulTypeRegistry registry, final StateStore store, final CartQuery queries) {
    this.store = store;
    this.cartQuery = queries;

    registry
      .register(new Info(store, CartUserSummaryData.class, CartUserSummaryData.class.getSimpleName()));
  }
}
