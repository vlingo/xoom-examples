package io.vlingo.xoom.examples.ecommerce.infra;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.examples.ecommerce.model.CartUserSummaryData;
import io.vlingo.xoom.examples.ecommerce.model.UserId;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.StateAdapter;


public class UserIdStateAdapter implements StateAdapter<UserId, State.TextState> {

    @Override
    public int typeVersion() {
        return 0;
    }

    @Override
    public UserId fromRawState(State.TextState textState) {
        return JsonSerialization.deserialized(textState.data, textState.typed());
    }

    @Override
    public <ST> ST fromRawState(State.TextState textState, Class<ST> aClass) {
        return JsonSerialization.deserialized(textState.data, aClass);
    }

    @Override
    public State.TextState toRawState(String id, UserId state, int stateVersion, Metadata metadata) {
        final String serialization = JsonSerialization.serialized(state);
        return new State.TextState(id, CartUserSummaryData.class, typeVersion(), serialization, stateVersion, metadata);
    }
}
