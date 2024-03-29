// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.examples.perf.vlingo.model.greeting.GreetingState;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.common.jdbc.Configuration;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.storage.DatabaseParameters;
import io.vlingo.xoom.turbo.storage.Model;

public class CommandModelStateStoreProvider {
  private static CommandModelStateStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;

  public static CommandModelStateStoreProvider instance() {
    return instance;
  }

  public static CommandModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry){
    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    return using(stage, registry, noop);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static CommandModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(GreetingState.class, new GreetingStateAdapter());

    new EntryAdapterProvider(stage.world()); // future use

    final Configuration configuration = new DatabaseParameters(Model.COMMAND, Settings.properties(), true)
            .mapToConfiguration();

    Tuple2<StateStore, DispatcherControl> storeWithControl = StorageProvider.storeWithControl(stage, configuration, dispatcher);

    registry.register(new Info(storeWithControl._1, GreetingState.class, GreetingState.class.getSimpleName()));
    instance = new CommandModelStateStoreProvider(storeWithControl._1, storeWithControl._2);

    return instance;
  }

  private CommandModelStateStoreProvider(final StateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;
  }
}
