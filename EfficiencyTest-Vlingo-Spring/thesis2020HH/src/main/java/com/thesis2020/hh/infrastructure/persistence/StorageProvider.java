/**
 * 
 */
package com.thesis2020.hh.infrastructure.persistence;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import io.vlingo.actors.Actor;
import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Tuple2;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.DispatcherControl.DispatcherControlDelegate;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.dispatch.control.DispatcherControlActor;
import io.vlingo.symbio.store.state.PartitioningStateStore;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.jdbc.JDBCEntriesBatchWriter;
import io.vlingo.symbio.store.state.jdbc.JDBCEntriesInstantWriter;
import io.vlingo.symbio.store.state.jdbc.JDBCEntriesWriter;
import io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor;
import io.vlingo.symbio.store.state.jdbc.JDBCStorageDelegate;
import io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.storage.DatabaseParameters;
import io.vlingo.xoom.storage.Model;

/**
 * @author hadydab
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked"})
public enum StorageProvider {
	
	
	INSTANCE;
	
	
	static final long DefaultCheckConfirmationExpirationInterval = 1000;
	static final long DefaultConfirmationExpiration = 1000;
	
	
	public Tuple2<StateStore, DispatcherControl> storeWithPartitioningWithNoOp(final Stage stage,final Model model,final String mailBox){
		return storeWithPartitioning(stage,model,new NoOpDispatcher(),mailBox);
	}
	
	public Tuple2<StateStore, DispatcherControl> storeWithPartitioningWithControl(final Stage stage,final Model model,final Dispatcher dispatcher,
			final String mailBox){
		return storeWithPartitioning(stage,model,dispatcher,mailBox);
	}
	
	public StateStore storeWithoutPartitioningWithNoOp(final Stage stage,final Model model,final String mailBoxName, final String actorName, final boolean createDb){
		return storeWithoutPartitioning(stage,new NoOpDispatcher(),model,mailBoxName,actorName,createDb);
	}
	
	
	
	private StateStore storeWithoutPartitioning(final Stage stage, final Dispatcher dispatcher, final Model model,
			final String mailBoxName, final String actorName, final boolean createDb) {
		final Configuration configuration = new DatabaseParameters(model, Settings.properties(), createDb)
				.mapToConfiguration();

		final JDBCStorageDelegate  delegate = new PostgresStorageDelegate(configuration, stage.world().defaultLogger());

		final DispatcherControl dispatcherControl = stage.actorFor(DispatcherControl.class,
				Definition.has(DispatcherControlActor.class,
						new DispatcherControl.DispatcherControlInstantiator(dispatcher,
								(DispatcherControlDelegate) delegate, DefaultCheckConfirmationExpirationInterval,
								DefaultConfirmationExpiration)));

		final JDBCEntriesWriter entriesWriter =
	            new JDBCEntriesInstantWriter(delegate,
	                    Arrays.asList(dispatcher), dispatcherControl);
		entriesWriter.setLogger(stage.world().defaultLogger());

		final ActorInstantiator<?> instantiator = new JDBCStateStoreActor.JDBCStateStoreInstantiator(delegate,
				(JDBCEntriesInstantWriter) entriesWriter, null);

		final StateStore stateStore = stage.actorFor(StateStore.class,
				Definition.has(JDBCStateStoreActor.class, instantiator, mailBoxName, actorName));

		return stateStore;
	}
	
	
	private  Tuple2<StateStore, DispatcherControl>  storeWithPartitioning(final Stage stage,final Model model,final Dispatcher dispatcher,
			final String mailBox){
		 final Configuration configuration = new DatabaseParameters(model, Settings.properties(), true)
		            .mapToConfiguration();
		 final StateStore.StorageDelegate delegate = new PostgresStorageDelegate(configuration, stage.world().defaultLogger());
		 
		 final DispatcherControl dispatcherControl = stage.actorFor(DispatcherControl.class,
	                Definition.has(DispatcherControlActor.class,
	                        new DispatcherControl.DispatcherControlInstantiator(dispatcher,
	                                (DispatcherControl.DispatcherControlDelegate) delegate,
	                                DefaultCheckConfirmationExpirationInterval,
	                                DefaultConfirmationExpiration)));
		 
		 PartitioningStateStore.InstantiatorProvider instantiatorProvider = new PartitioningStateStore.InstantiatorProvider() {
	            @Override
	            public <A extends Actor> Optional<Definition> definitionFor(Class<A> stateStoreActorType, ActorInstantiator<A> instantiator, PartitioningStateStore.StateStoreRole role, int currentPartition, int totalPartitions) {
	                return Optional.of(Definition.has(stateStoreActorType, instantiator, mailBox, "StateStore-" + role.name() + "-" + currentPartition));
	            }

	            @Override
	            public <A extends Actor> ActorInstantiator<A> instantiatorFor(Class<A> stateStoreActorType, PartitioningStateStore.StateStoreRole role, int currentPartition, int totalPartitions) {
	                final StateStore.StorageDelegate localDelegate = new PostgresStorageDelegate(Configuration.cloneOf(configuration), stage.world().defaultLogger());
	                final JDBCEntriesBatchWriter entriesWriter = new JDBCEntriesBatchWriter((JDBCStorageDelegate) localDelegate,
	                        Collections.singletonList(dispatcher), dispatcherControl, 100);
	                final JDBCStateStoreActor.JDBCStateStoreInstantiator instantiator = new JDBCStateStoreActor.JDBCStateStoreInstantiator(
	                        (JDBCStorageDelegate) localDelegate, entriesWriter, 150, null);

	                return (ActorInstantiator<A>) instantiator;
	            }
	        };
	        
	        return Tuple2.from(
	                PartitioningStateStore.using(stage, JDBCStateStoreActor.class, instantiatorProvider, 10, 10),
	                dispatcherControl);
		 
	}
	
	

}
