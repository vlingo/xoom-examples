/**
 * 
 */
package com.thesis2020.hh.infrastructure.persistence;

import com.thesis2020.hh.infrastructure.data.GreetingData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.StateAdapter;
import io.vlingo.symbio.State.TextState;

/**
 * @author hadydab
 *
 */
public class GreetingDataAdapter implements StateAdapter<GreetingData,TextState>{

	@Override
	public int typeVersion() {
		return 2;
	}

	@Override
	public GreetingData fromRawState(TextState raw) {
		return JsonSerialization.deserialized(raw.data, raw.typed());
	}

	@Override
	public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
		return JsonSerialization.deserialized(raw.data, stateType);
	}
	
	@Override
	public TextState toRawState(String id, GreetingData state, int stateVersion, Metadata metadata) {
		final String serialization = JsonSerialization.serialized(state);
		return new TextState(id, GreetingData.class, typeVersion(), serialization, stateVersion, metadata);
	}

}
