package io.vlingo.examples.ecommerce.infra;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartUserSummaryData;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State;
import io.vlingo.symbio.StateAdapter;


public class CartStateAdapter implements StateAdapter<CartUserSummaryData, State.TextState> {

    @Override
    public int typeVersion() {
        return 0;
    }

    @Override
    public CartUserSummaryData fromRawState(State.TextState textState) {
        return JsonSerialization.deserialized(textState.data, textState.typed());
    }

    @Override
    public <ST> ST fromRawState(State.TextState textState, Class<ST> aClass) {
        return JsonSerialization.deserialized(textState.data, aClass);
    }

    @Override
    public State.TextState toRawState(String id, CartUserSummaryData state, int stateVersion, Metadata metadata) {
        final String serialization = JsonSerialization.serialized(state);
        return new State.TextState(state.userId, CartUserSummaryData.class, typeVersion(), serialization, stateVersion, metadata);
    }
}
