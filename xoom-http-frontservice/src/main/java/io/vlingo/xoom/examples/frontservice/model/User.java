// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.model;

import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.xoom.common.Completes;

public interface User {
  void attachPrivateToken(final String privateToken);
  Completes<User.UserState> withContact(final Contact contact);
  Completes<User.UserState> withName(final Name name);


  static UserState nonExisting() {
    return new UserState(null, null, null, null);
  }

  static UserState from(final Name name, final Contact contact, final Security security) {
    return new UserState(nextId(), name, contact, security);
  }

  static UserState from(final String id, final Name name, final Contact contact, final Security security) {
    return new UserState(id, name, contact, security);
  }

  static void resetId() {
    UserState.NextId.set(0);
  }

  public static String nextId() {
    final int id = UserState.NextId.incrementAndGet();
    return String.format("%03d", id);
  }

  public static final class UserState {
    private static final AtomicInteger NextId = new AtomicInteger(0);

    public static UserState of(String id) {
      return new UserState(id, null, null, null);
    }

    public final String id;
    public final Name name;
    public final Contact contact;
    public final Security security;
    public final int version;
    
    public boolean doesNotExist() {
      return id == null;
    }

    public boolean isIdentifiedOnly() {
      return id != null && name == null && contact == null && security == null;
    }

    public UserState withContact(final Contact contact) {
      return new UserState(this.id, this.name, contact, this.security, version + 1);
    }

    public UserState withName(final Name name) {
      return new UserState(this.id, name, this.contact, this.security, version + 1);
    }

    public UserState withSecurity(final Security security) {
      return new UserState(this.id, this.name, this.contact, security, version + 1);
    }

    @Override
    public String toString() {
      return "User.State[id=" + id + ", name=" + name + ", contact=" + contact + "]";
    }

    private UserState(final String id, final Name name, final Contact contact, final Security security) {
      this.id = id;
      this.name = name;
      this.contact = contact;
      this.security = security;
      this.version = 1;
    }

    private UserState(final String id, final Name name, final Contact contact, final Security security, final int version) {
      this.id = id;
      this.name = name;
      this.contact = contact;
      this.security = security;
      this.version = version;
    }
  }
}
