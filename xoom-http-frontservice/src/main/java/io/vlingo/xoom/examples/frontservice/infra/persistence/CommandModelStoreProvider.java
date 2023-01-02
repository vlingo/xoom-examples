// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.infra.persistence;

import java.util.Arrays;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.examples.frontservice.data.ProfileData;
import io.vlingo.xoom.examples.frontservice.data.UserData;
import io.vlingo.xoom.examples.frontservice.model.Profile;
import io.vlingo.xoom.examples.frontservice.model.Profile.ProfileState;
import io.vlingo.xoom.examples.frontservice.model.User;
import io.vlingo.xoom.examples.frontservice.model.User.UserState;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class CommandModelStoreProvider {
  private static CommandModelStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;

  public static CommandModelStoreProvider instance() {
    return instance;
  }

  @SuppressWarnings("rawtypes")
  public static CommandModelStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) return instance;
    
    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(UserState.class, new UserStateAdapter());
    stateAdapterProvider.registerAdapter(ProfileState.class, new ProfileStateAdapter());
    stateAdapterProvider.registerAdapter(UserData.class, new UserDataStateAdapter());
    stateAdapterProvider.registerAdapter(ProfileData.class, new ProfileDataStateAdapter());
    new EntryAdapterProvider(stage.world()); // future

    final Protocols storeProtocols =
            stage.actorFor(
                    new Class<?>[] { StateStore.class, DispatcherControl.class },
                    Definition.has(InMemoryStateStoreActor.class, Definition.parameters(Arrays.asList(dispatcher))));

    final Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

    instance = new CommandModelStoreProvider(registry, storeWithControl._1, storeWithControl._2);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CommandModelStoreProvider(final StatefulTypeRegistry registry, final StateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;

    registry
      .register(new Info(store, User.UserState.class, User.UserState.class.getSimpleName()))
      .register(new Info(store, Profile.ProfileState.class, Profile.ProfileState.class.getSimpleName()))
      .register(new Info(store, UserData.class, UserData.class.getSimpleName()))
      .register(new Info(store, ProfileData.class, ProfileData.class.getSimpleName()));
  }
}
