/**
 * 
 */
package com.thesis2020.hh.infrastructure.persistence;

import com.thesis2020.hh.infrastructure.data.GreetingData;
import com.thesis2020.hh.model.greeting.GreetingEvents;
import com.thesis2020.hh.model.greeting.GreetingState;

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.store.state.StateStore;

/**
 * @author hadydab
 *
 */
public class GreetingProjectionActor extends StateStoreProjectionActor<GreetingData> {
	
	
	private GreetingEvents eventOf;
	
	public GreetingProjectionActor(StateStore stateStore) {
		super(stateStore);
	}
	
	
	@Override
	protected GreetingData currentDataFor(Projectable projectable) {
		
		eventOf = GreetingEvents.valueOf(projectable.becauseOf()[0]);
		final GreetingState state = projectable.object();
	    final GreetingData current = GreetingData.from(state);
		
		return current;
	}
	
	
	@Override
	protected GreetingData merge(GreetingData previousData, int previousVersion, GreetingData currentData,
			int currentVersion) {
		
		GreetingData merged;
		
		switch (eventOf) {
		case GreetingDefined:
			merged = currentData;
			break;
		case GreetingMessageChange:
			merged = GreetingData.from(previousData.id, currentData.message, currentData.messageChangedCount, previousData.description, previousData.descriptionChangedCount);
			break;
		case GreetingDescriptionChanged:
			merged = GreetingData.from(previousData.id, previousData.message, previousData.messageChangedCount, currentData.description, currentData.descriptionChangedCount);
		      break;
		default:
			merged = currentData;
			break;
		}
		
		return merged;
	}

}
