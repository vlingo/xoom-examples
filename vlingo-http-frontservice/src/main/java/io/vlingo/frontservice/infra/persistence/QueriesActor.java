// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import java.util.Arrays;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

public class QueriesActor extends StateStoreQueryActor implements Queries {
  public QueriesActor(final StateStore store) {
    super(store);
  }

  @Override
  public Completes<ProfileData> profileOf(final String userId) {
    return queryStateFor(userId, ProfileData.class, ProfileData.empty());
  }

  @Override
  public Completes<UserData> userDataOf(final String userId) {
    return queryStateFor(userId, UserData.class, UserData.empty());
  }

  @Override
  public Completes<Collection<UserData>> usersData() {
    return completes().with(Arrays.asList()); // TODO: implement
  }
}
