// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.actors.Completes;
import io.vlingo.actors.CompletesEventually;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public class UserEntity extends StatefulEntity<User.State,String> implements User {
  private User.State state;
  private int stateVersion;

  public UserEntity(final User.State state) {
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
    final User.State transitioned = state.withSecurity(state.security.withPrivateToken(privateToken));
    preserve(transitioned);
  }

  public Completes<User.State> withContact(final Contact contact) {
    final CompletesEventually completes = completesEventually();
    final User.State transitioned = state.withContact(contact);
    preserve(transitioned, "User:contact", (state, version) -> {
      state(state, version);
      completes.with(state);
    });
    return completes(); // unanswered until preserved
  }

  public Completes<User.State> withName(final Name name) {
    final CompletesEventually completes = completesEventually();
    final User.State transitioned = state.withName(name);
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
  public void state(final State state, final int stateVersion) {
    this.state = state;
    this.stateVersion = stateVersion;
  }

  @Override
  public Class<?> stateType() {
    return User.State.class;
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
