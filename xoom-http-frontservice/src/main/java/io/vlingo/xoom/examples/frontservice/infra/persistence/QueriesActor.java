// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.infra.persistence;

import java.util.Arrays;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.frontservice.data.ProfileData;
import io.vlingo.xoom.examples.frontservice.data.UserData;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

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
