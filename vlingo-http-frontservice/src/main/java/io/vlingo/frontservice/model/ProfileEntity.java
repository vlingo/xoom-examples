// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.symbio.State.TextState;

public class ProfileEntity extends StatefulEntity<Profile.ProfileState,TextState> implements Profile {
  private Profile.ProfileState state;
  private int stateVersion;

  public ProfileEntity(final Profile.ProfileState state) {
    this.state = state;
  }

  @Override
  public void start() {
    if (state.isIdentifiedOnly()) {
      restore((state, version) -> state(state, version));
    } else {
      preserve(state, "Profile:new");
    }
  }

  @Override
  public Completes<Profile.ProfileState> withTwitterAccount(final String twitterAccount) {
    final Profile.ProfileState transitioned = state.withTwitterAccount(twitterAccount);
    preserve(transitioned, "Profile:twitter", (state, version) -> state(state, version));
    return completes().with(transitioned);
  }

  @Override
  public Completes<Profile.ProfileState> withLinkedInAccount(final String linkedInAccount) {
    final Profile.ProfileState transitioned = state.withLinkedInAccount(linkedInAccount);
    preserve(transitioned, "Profile:linkedIn", (state, version) -> state(state, version));
    return completes().with(transitioned);
  }

  @Override
  public Completes<Profile.ProfileState> withWebSite(final String website) {
    final Profile.ProfileState transitioned = state.withWebSite(website);
    preserve(transitioned, "Profile:website", (state, version) -> state(state, version));
    return completes().with(transitioned);
  }


  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  public String id() {
    return state.id;
  }

  @Override
  public void state(final ProfileState state, final int stateVersion) {
    this.state = state;
    this.stateVersion = stateVersion;
  }

  @Override
  public Class<?> stateType() {
    return Profile.ProfileState.class;
  }

  @Override
  public int stateVersion() {
    return stateVersion;
  }
}
