// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.actors.CompletesEventually;
import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.symbio.State.TextState;

public class UserEntity extends StatefulEntity<User.UserState,TextState> implements User {
  private User.UserState state;
  private int stateVersion;

  public UserEntity(final User.UserState state) {
    this.state = state;
  }

  @Override
  public void start() {
    if (state.isIdentifiedOnly()) {
      restore((state, version) -> state(state, version));
    } else {
      preserve(state, "User:new");
    }
  }


  //=====================================
  // User
  //=====================================

  @Override
  public void attachPrivateToken(final String privateToken) {
    final User.UserState transitioned = state.withSecurity(state.security.withPrivateToken(privateToken));
    preserve(transitioned);
  }

  public Completes<User.UserState> withContact(final Contact contact) {
    final CompletesEventually completes = completesEventually();
    final User.UserState transitioned = state.withContact(contact);
    preserve(transitioned, "User:contact", (state, version) -> {
      state(state, version);
      completes.with(state);
    });
    return completes(); // unanswered until preserved
  }

  public Completes<User.UserState> withName(final Name name) {
    final CompletesEventually completes = completesEventually();
    final User.UserState transitioned = state.withName(name);
    preserve(transitioned, "User:name", (state, version) -> {
      state(state, version);
      completes.with(state);
    });
    return completes(); // unanswered until preserved
  }


  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  public String id() {
    return state.id;
  }

  @Override
  public void state(final UserState state, final int stateVersion) {
    this.state = state;
    this.stateVersion = stateVersion;
  }

  @Override
  public Class<?> stateType() {
    return User.UserState.class;
  }

  @Override
  public int stateVersion() {
    return stateVersion;
  }
}
