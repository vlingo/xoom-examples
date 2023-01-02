// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.examples.perf.vlingo.model.greeting.GreetingState;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State.TextState;
import io.vlingo.xoom.symbio.StateAdapter;

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
