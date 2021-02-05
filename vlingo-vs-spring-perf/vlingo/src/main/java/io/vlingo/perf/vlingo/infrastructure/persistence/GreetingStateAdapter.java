package io.vlingo.perf.vlingo.infrastructure.persistence;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.perf.vlingo.model.greeting.GreetingState;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.StateAdapter;

public final class GreetingStateAdapter implements StateAdapter<GreetingState, TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  @Override
  public GreetingState fromRawState(final TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public <ST> ST fromRawState(final TextState raw, final Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, stateType);
  }

  @Override
  public TextState toRawState(final String id, final GreetingState state, final int stateVersion, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(id, GreetingState.class, typeVersion(), serialization, stateVersion, metadata);
  }
}
