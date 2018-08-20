// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.actors.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public class ProfileEntity extends StatefulEntity<Profile.State,String> implements Profile {
  private Profile.State state;
  private int stateVersion;

  public ProfileEntity(final Profile.State state) {
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
  public Completes<Profile.State> withTwitterAccount(final String twitterAccount) {
    final Profile.State transitioned = state.withTwitterAccount(twitterAccount);
    preserve(transitioned, "Profile:twitter", (state, version) -> state(state, version));
    return completes().with(transitioned);
  }

  @Override
  public Completes<Profile.State> withLinkedInAccount(final String linkedInAccount) {
    final Profile.State transitioned = state.withLinkedInAccount(linkedInAccount);
    preserve(transitioned, "Profile:linkedIn", (state, version) -> state(state, version));
    return completes().with(transitioned);
  }

  @Override
  public Completes<Profile.State> withWebSite(final String website) {
    final Profile.State transitioned = state.withWebSite(website);
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
  public void state(final State state, final int stateVersion) {
    this.state = state;
    this.stateVersion = stateVersion;
  }

  @Override
  public Class<?> stateType() {
    return Profile.State.class;
  }

  @Override
  public int stateVersion() {
    return stateVersion;
  }

  @Override
  public int typeVersion() {
    return 1;
  }
}
