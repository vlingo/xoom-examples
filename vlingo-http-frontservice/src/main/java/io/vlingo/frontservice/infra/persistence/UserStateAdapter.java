// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.frontservice.model.User;
import io.vlingo.frontservice.model.User.UserState;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.StateAdapter;

public class UserStateAdapter implements StateAdapter<User.UserState,TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  @Override
  public UserState fromRawState(final TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public TextState toRawState(String id, UserState state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(id, UserState.class, typeVersion(), serialization, stateVersion, metadata);
  }

  @Override
  public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, stateType);
  }
}
