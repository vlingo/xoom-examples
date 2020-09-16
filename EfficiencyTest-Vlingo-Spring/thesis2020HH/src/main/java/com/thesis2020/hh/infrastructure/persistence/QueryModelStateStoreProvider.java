package com.thesis2020.hh.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;

import com.thesis2020.hh.model.greeting.GreetingState;
import io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;
import io.vlingo.actors.Definition;
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

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.symbio.store.DataFormat;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore.StorageDelegate;
import io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor.JDBCStateStoreInstantiator;
import io.vlingo.symbio.store.common.jdbc.Configuration;


public class QueryModelStateStoreProvider {
  private static QueryModelStateStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;
  public final Queries queries;

  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) throws Exception {
    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    return using(stage, registry, noop);
  }

  @SuppressWarnings("rawtypes")
  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) throws Exception {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(GreetingState.class, new GreetingStateAdapter());

    new EntryAdapterProvider(stage.world()); // future use

//    final ActorInstantiator jdbcInstantiator = setupJDBCInstantiator(stage, dispatcher);
    
//    final Protocols storeProtocols =
//            stage.actorFor(
//                    new Class<?>[] { StateStore.class, DispatcherControl.class },
//                    Definition.has(io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor.class, 
//                    		jdbcInstantiator,"jdbcQueueMailbox","queryModelStateStore"));
    
    final Protocols storeProtocols =
            StoreActorBuilder.from(stage, Model.QUERY, dispatcher, StorageType.STATE_STORE, Settings.properties(), true);

    final Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);
   
    registry.register(new Info(storeWithControl._1, GreetingState.class, GreetingState.class.getSimpleName()));
    
    Queries queries = stage.actorFor(Queries.class, QueriesActor.class,storeWithControl._1);
    
    instance = new QueryModelStateStoreProvider(storeWithControl._1, storeWithControl._2,queries);

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
//    return new PostgresStorageDelegate(DatabaseConfiguration.load(Model.QUERY), stage.world().defaultLogger());
//  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStateStoreProvider(final StateStore store, final DispatcherControl dispatcherControl,final Queries queries) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;
    this.queries = queries;
  }
}
