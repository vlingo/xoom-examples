package com.thesis2020.hh.infrastructure.persistence;

import com.thesis2020.hh.model.greeting.GreetingState;
import io.vlingo.actors.Stage;
import io.vlingo.common.Tuple2;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.storage.Model;

@SuppressWarnings({ "unchecked", "rawtypes" })
public enum CommandModelStateStoreProvider {
	INSTANCE;
	
	private StateStore statestore;
	private DispatcherControl dispatcherControl;

	
	public void using(final Stage stage, final StatefulTypeRegistry registry,final Dispatcher dispatcher) {
		
		final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
		stateAdapterProvider.registerAdapter(GreetingState.class, new GreetingStateAdapter());
		
		StateTypeStateStoreMap.stateTypeToStoreName(GreetingState.class, GreetingState.class.getSimpleName());
		
		final Tuple2<StateStore, DispatcherControl> storeWithPartitioningAndControl = 
	    		StorageProvider.INSTANCE.storeWithPartitioningWithControl(stage, Model.COMMAND,
	    				dispatcher,"jdbcQueueMailbox");
		
		new EntryAdapterProvider(stage.world()); // future use
		registry.register(new Info(storeWithPartitioningAndControl._1, GreetingState.class, GreetingState.class.getSimpleName()));
		
		this.statestore = storeWithPartitioningAndControl._1;
		this.dispatcherControl = storeWithPartitioningAndControl._2;
	}
	
	
	public StateStore store() {
		return statestore;
	}
	
	public DispatcherControl control() {
		return dispatcherControl;
	}

}
