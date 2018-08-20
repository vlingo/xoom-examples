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
import io.vlingo.symbio.store.state.StateStore.DispatcherControl;
import io.vlingo.symbio.store.state.TextStateStore;
import io.vlingo.symbio.store.state.TextStateStore.TextDispatcher;
import io.vlingo.symbio.store.state.inmemory.InMemoryTextStateStoreActor;

public class CommandModelStoreProvider {
  private static CommandModelStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final TextStateStore store;

  public static CommandModelStoreProvider instance() {
    return instance;
  }

  public static CommandModelStoreProvider using(final Stage stage, final TextDispatcher dispatcher) {
    if (instance != null) return instance;

    final Protocols storeProtocols =
            stage.actorFor(
                    Definition.has(InMemoryTextStateStoreActor.class, Definition.parameters(dispatcher)),
                    new Class<?>[] { TextStateStore.class, DispatcherControl.class });

    final Protocols.Two<TextStateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

    instance = new CommandModelStoreProvider(storeWithControl._1, storeWithControl._2);

    return instance;
  }

  private CommandModelStoreProvider(final TextStateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;

    StatefulTypeRegistry.instance
      .register(new Info<User.State,String>(store, User.State.class, User.State.class.getSimpleName(), new UserStateAdapter()))
      .register(new Info<Profile.State,String>(store, Profile.State.class, Profile.State.class.getSimpleName(), new ProfileStateAdapter()));
  }
}
