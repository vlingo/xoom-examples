// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple3;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.symbio.Source;

public class UserEntity extends StatefulEntity<User.UserState> implements User {
  private User.UserState state;

  public UserEntity(final User.UserState state) {
    this.state = state;
  }


  //=====================================
  // User
  //=====================================

  @Override
  public void attachPrivateToken(final String privateToken) {
    apply(state.withSecurity(state.security.withPrivateToken(privateToken)), "User:attachPrivateToken");
  }

  @Override
  public Completes<User.UserState> withContact(final Contact contact) {
    return apply(state.withContact(contact), "User:contact", () -> state);
  }

  @Override
  public Completes<User.UserState> withName(final Name name) {
    return apply(state.withName(name), "User:name", () -> state);
  }


  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  protected String id() {
    return state.id;
  }

  @Override
  protected void state(final UserState state) {
    this.state = state;
  }

  @Override
  protected Class<UserState> stateType() {
    return User.UserState.class;
  }

  @Override
  protected <C> Tuple3<User.UserState,List<Source<C>>,String> whenNewState() {
    if (state.isIdentifiedOnly()) {
      return null;
    }
    return Tuple3.from(state, Collections.emptyList(), "User:new");
  }

  @Override
  public void applyRelocationSnapshot(String snapshot) {
    state(UserState.of(snapshot));
  }
}
