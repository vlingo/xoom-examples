// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.frontservice.model.Profile;
import io.vlingo.frontservice.model.User;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateStore.Dispatcher;
import io.vlingo.symbio.store.state.StateStore.DispatcherControl;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class CommandModelStoreProvider {
  private static CommandModelStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;

  public static CommandModelStoreProvider instance() {
    return instance;
  }

  public static CommandModelStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) return instance;

    final Protocols storeProtocols =
            stage.actorFor(
                    Definition.has(InMemoryStateStoreActor.class, Definition.parameters(dispatcher)),
                    new Class<?>[] { StateStore.class, DispatcherControl.class });

    final Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

    instance = new CommandModelStoreProvider(registry, storeWithControl._1, storeWithControl._2);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CommandModelStoreProvider(final StatefulTypeRegistry registry, final StateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;

    registry
      .register(new Info(store, User.UserState.class, User.UserState.class.getSimpleName(), new UserStateAdapter()))
      .register(new Info(store, Profile.ProfileState.class, Profile.ProfileState.class.getSimpleName(), new ProfileStateAdapter()));
  }
}
