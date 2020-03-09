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
  private String becauseOf;

  public ProfileProjectionActor() {
    super(QueryModelStoreProvider.instance().store);
  }

  @Override
  protected ProfileData currentDataFor(final Projectable projectable) {
    becauseOf = projectable.becauseOf()[0];
    
    final Profile.ProfileState state = projectable.object();
    final ProfileData data = ProfileData.from(state);
    
    return data;
  }

  @Override
  protected ProfileData merge(final ProfileData previousData, final int previousVersion, final ProfileData currentData, final int currentVersion) {
    ProfileData merged;
    
    switch (becauseOf) {
    case "Profile:new":
      merged = currentData;
      break;
    case "Profile:twitter":
      merged = ProfileData.from(previousData.id, currentData.twitterAccount, previousData.linkedInAccount, previousData.website);
      break;
    case "Profile:linkedIn":
      merged = ProfileData.from(previousData.id, previousData.twitterAccount, currentData.linkedInAccount, previousData.website);
      break;
    case "Profile:website":
      merged = ProfileData.from(previousData.id, previousData.twitterAccount, previousData.linkedInAccount, currentData.website);
      break;
    default:
      merged = currentData;
      break;
    }
    
    return merged;
  }
}
