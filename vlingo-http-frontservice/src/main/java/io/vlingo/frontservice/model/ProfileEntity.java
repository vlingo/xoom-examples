// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public class ProfileEntity extends StatefulEntity<Profile.ProfileState> implements Profile {
  private Profile.ProfileState state;

  public ProfileEntity(final Profile.ProfileState state) {
    this.state = state;
  }

  @Override
  public void start() {
    if (state.isIdentifiedOnly()) {
      restore();
    } else {
      apply(state, "Profile:new");
    }
  }

  @Override
  public Completes<Profile.ProfileState> withTwitterAccount(final String twitterAccount) {
	return apply(state.withTwitterAccount(twitterAccount), "Profile:twitter", () -> state);
  }

  @Override
  public Completes<Profile.ProfileState> withLinkedInAccount(final String linkedInAccount) {
	return apply(state.withLinkedInAccount(linkedInAccount), "Profile:linkedIn", () -> state);
  }

  @Override
  public Completes<Profile.ProfileState> withWebSite(final String website) {
    return apply(state.withWebSite(website), "Profile:website", () -> state);
  }


  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  public String id() {
    return state.id;
  }

  @Override
  public void state(final ProfileState state) {
    this.state = state;
  }

  @Override
  public Class<ProfileState> stateType() {
    return ProfileState.class;
  }
}
