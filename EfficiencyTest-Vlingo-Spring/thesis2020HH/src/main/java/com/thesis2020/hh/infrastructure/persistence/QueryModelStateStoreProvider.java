package com.thesis2020.hh.infrastructure.persistence;

import com.thesis2020.hh.infrastructure.data.GreetingData;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.storage.Model;

public enum QueryModelStateStoreProvider {
	
	INSTANCE;
	
	public Queries queris;
	public StateStore statestore;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void using(final Stage stage, final StatefulTypeRegistry registry) {
		
		final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
		
		stateAdapterProvider.registerAdapter(GreetingData.class, new GreetingDataAdapter());
		
		StateTypeStateStoreMap.stateTypeToStoreName(GreetingData.class, GreetingData.class.getSimpleName());
		
		new EntryAdapterProvider(stage.world()); // future use
		
		this.statestore  = StorageProvider.INSTANCE.storeWithoutPartitioningWithNoOp(
				stage, Model.QUERY,"jdbcQueueMailbox","queryStateStoreActor", true);
		
		registry.register(new Info(statestore, GreetingData.class, GreetingData.class.getSimpleName()));
		
		this.queris = stage.actorFor(Queries.class, QueriesActor.class,statestore);
	}
	

}
