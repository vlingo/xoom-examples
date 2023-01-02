// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.infra.persistence;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.examples.frontservice.model.Profile;
import io.vlingo.xoom.examples.frontservice.model.Profile.ProfileState;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State.TextState;
import io.vlingo.xoom.symbio.StateAdapter;

public class ProfileStateAdapter implements StateAdapter<Profile.ProfileState,TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  @Override
  public ProfileState fromRawState(final TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public TextState toRawState(final ProfileState state, final int stateVersion, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(state.id, ProfileState.class, typeVersion(), serialization, stateVersion, metadata);
  }

  @Override
  public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, stateType);
  }
}
