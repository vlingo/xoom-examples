package com.saasovation.collaboration.infra.persistence;

import com.saasovation.collaboration.model.forum.Forum;
import com.saasovation.collaboration.model.forum.Forum.State;
import com.saasovation.collaboration.model.forum.ForumEntity;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.StateAdapter;

public class SnapshotStateAdapters {
  public static class ForumStateAdapter implements StateAdapter<Forum.State,TextState> {

    @Override
    public int typeVersion() {
      return 1;
    }

    @Override
    public State fromRawState(final TextState raw) {
      return JsonSerialization.deserialized(raw.data, raw.typed());
    }

    @Override
    public TextState toRawState(State state, int stateVersion, Metadata metadata) {
      final String serialization = JsonSerialization.serialized(state);
      return new TextState(TextState.NoOp, ForumEntity.State.class, typeVersion(), serialization, stateVersion, metadata);
    }

    @Override
    public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
      return JsonSerialization.deserialized(raw.data, stateType);
    }
  }
}
