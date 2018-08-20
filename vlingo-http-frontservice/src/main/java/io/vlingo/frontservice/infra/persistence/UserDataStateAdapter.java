// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.lattice.model.stateful.StateAdapter;

public class UserDataStateAdapter implements StateAdapter<UserData,String> {

  @Override
  public UserData from(final String raw, final int stateVersion, final int typeVersion) {
    return JsonSerialization.deserialized(raw, UserData.class);
  }

  @Override
  public String to(final UserData state, final int stateVersion, final int typeVersion) {
    return JsonSerialization.serialized(state);
  }
}
