// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.Profile;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;

public class ProfileProjectionActor extends StateStoreProjectionActor<ProfileData> {
  public ProfileProjectionActor() {
    super(QueryModelStoreProvider.instance().store);
  }

  @Override
  protected ProfileData currentDataFor(final Projectable projectable) {
    final Profile.ProfileState state = projectable.object();
    final ProfileData data = ProfileData.from(state);

// TODO: for you to complete the implementation
//    switch (projectable.becauseOf()[0]) {
//    case "Profile:new":
//      data = ProfileData.from(state);
//      break;
//    case "Profile:twitter":
//    case "Profile:linkedIn":
//    case "Profile:website":
//      break;
//    }
    
    return data;
  }

  @Override
  protected ProfileData merge(final ProfileData previousData, final int previousVersion, final ProfileData currentData, final int currentVersion) {
    return currentData;
  }
}
