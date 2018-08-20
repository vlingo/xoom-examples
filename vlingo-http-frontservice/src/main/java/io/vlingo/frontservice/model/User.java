// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import java.util.concurrent.atomic.AtomicInteger;

import io.vlingo.actors.Completes;

public interface User {
  Completes<User.State> withContact(final Contact contact);
  Completes<User.State> withName(final Name name);

  static State nonExisting() {
    return new State(null, null, null, null);
  }

  static State from(final Name name, final Contact contact, final Security security) {
    return new State(nextId(), name, contact, security);
  }

  static State from(final String id, final Name name, final Contact contact, final Security security) {
    return new State(id, name, contact, security);
  }

  static void resetId() {
    State.NextId.set(0);
  }

  public static String nextId() {
    final int id = State.NextId.incrementAndGet();
    return String.format("%03d", id);
  }

  public static final class State {
    private static final AtomicInteger NextId = new AtomicInteger(0);

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

    public State withContact(final Contact contact) {
      return new State(this.id, this.name, contact, this.security, version + 1);
    }

    public State withName(final Name name) {
      return new State(this.id, name, this.contact, this.security, version + 1);
    }

    public State withSecurity(final Security security) {
      return new State(this.id, this.name, this.contact, security, version + 1);
    }

    @Override
    public String toString() {
      return "User.State[id=" + id + ", name=" + name + ", contact=" + contact + "]";
    }

    private State(final String id, final Name name, final Contact contact, final Security security) {
      this.id = id;
      this.name = name;
      this.contact = contact;
      this.security = security;
      this.version = 1;
    }

    private State(final String id, final Name name, final Contact contact, final Security security, final int version) {
      this.id = id;
      this.name = name;
      this.contact = contact;
      this.security = security;
      this.version = version;
    }
  }
}
