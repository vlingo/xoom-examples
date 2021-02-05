package io.vlingo.perf.vlingo.infrastructure.persistence;

import io.vlingo.actors.Stage;
import io.vlingo.common.Tuple2;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.perf.vlingo.model.greeting.GreetingState;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.storage.DatabaseParameters;
import io.vlingo.xoom.storage.Model;

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
