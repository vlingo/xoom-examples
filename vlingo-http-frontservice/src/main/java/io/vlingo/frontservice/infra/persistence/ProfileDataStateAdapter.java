// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.lattice.model.stateful.StateAdapter;

public class ProfileDataStateAdapter implements StateAdapter<ProfileData,String> {

  @Override
  public ProfileData from(final String raw, final int stateVersion, final int typeVersion) {
    return JsonSerialization.deserialized(raw, ProfileData.class);
  }

  @Override
  public String to(final ProfileData state, final int stateVersion, final int typeVersion) {
    return JsonSerialization.serialized(state);
  }
}
