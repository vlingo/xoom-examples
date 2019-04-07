// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public class UserEntity extends StatefulEntity<User.UserState> implements User {
  private User.UserState state;

  public UserEntity(final User.UserState state) {
    this.state = state;
  }

  @Override
  public void start() {
    if (state.isIdentifiedOnly()) {
      restore();
    } else {
      apply(state, "User:new");
    }
  }


  //=====================================
  // User
  //=====================================

  @Override
  public void attachPrivateToken(final String privateToken) {
    final User.UserState transitioned = state.withSecurity(state.security.withPrivateToken(privateToken));
    apply(transitioned);
  }

  @Override
  public Completes<User.UserState> withContact(final Contact contact) {
    apply(state.withContact(contact), "User:contact", () -> state);
    return completes();
  }

  @Override
  public Completes<User.UserState> withName(final Name name) {
    apply(state.withName(name), "User:name", () -> state);
    return completes();
  }


  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  public String id() {
    return state.id;
  }

  @Override
  public void state(final UserState state) {
    this.state = state;
  }

  @Override
  public Class<UserState> stateType() {
    return User.UserState.class;
  }
}
