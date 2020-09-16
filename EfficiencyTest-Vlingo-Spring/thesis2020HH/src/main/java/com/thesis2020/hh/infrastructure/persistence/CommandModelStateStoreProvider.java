package com.thesis2020.hh.infrastructure.persistence;


import com.thesis2020.hh.model.greeting.GreetingState;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.state.StateStore;



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

  @SuppressWarnings("rawtypes")
  public static CommandModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(GreetingState.class, new GreetingStateAdapter());

    new EntryAdapterProvider(stage.world()); // future use

//    final ActorInstantiator jdbcInstantiator = setupJDBCInstantiator(stage, dispatcher);
//    final List<Object> parameters = Definition.parameters(Arrays.asList(jdbcInstantiator));
//
//    final Protocols storeProtocols =
//            stage.actorFor(
//                    new Class<?>[] { StateStore.class, DispatcherControl.class },
//                    Definition.has(io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor.class, 
//                    		jdbcInstantiator,"jdbcQueueMailbox","commandModelStateStore"));
    final Protocols storeProtocols =
            StoreActorBuilder.from(stage, Model.COMMAND, dispatcher, StorageType.STATE_STORE, Settings.properties(), true);
    
    final Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

    registry.register(new Info(storeWithControl._1, GreetingState.class, GreetingState.class.getSimpleName()));
    instance = new CommandModelStateStoreProvider(storeWithControl._1, storeWithControl._2);

    return instance;
  }

//  private static ActorInstantiator setupJDBCInstantiator(final Stage stage, final Dispatcher dispatcher) throws Exception {
//    final StorageDelegate storageDelegate = setupStorageDelegate(stage);
//    final ActorInstantiator<?> instantiator = new JDBCStateStoreInstantiator();
//    instantiator.set("dispatcher", dispatcher);
//    instantiator.set("delegate", storageDelegate);
//    return instantiator;
//  }

//  private static StorageDelegate setupStorageDelegate(final Stage stage) throws Exception {
//    return new PostgresStorageDelegate(DatabaseConfiguration.load(Model.COMMAND), stage.world().defaultLogger());
//  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CommandModelStateStoreProvider(final StateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;
  }
}
