// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.actors.Completes;

public interface Profile {
  Completes<Profile.State> withTwitterAccount(final String twitterAccount);
  Completes<Profile.State> withLinkedInAccount(final String linkedInAccount);
  Completes<Profile.State> withWebSite(final String website);

  public static State from(final String id, final String twitterAccount, final String linkedInAccount, final String website) {
    return new State(id, twitterAccount, linkedInAccount, website);
  }

  public static State nonExisting() {
    return new State(null, null, null, null);
  }

  public static final class State {
    public final String id;
    public final String linkedInAccount;
    public final String twitterAccount;
    public final String website;

    public boolean doesNotExist() {
      return id == null;
    }

    public boolean isIdentifiedOnly() {
      return id != null && linkedInAccount == null && twitterAccount == null && website == null;
    }

    State withTwitterAccount(final String twitterAccount) {
      return new State(id, twitterAccount, linkedInAccount, website);
    }

    State withLinkedInAccount(final String linkedInAccount) {
      return new State(id, twitterAccount, linkedInAccount, website);
    }

    State withWebSite(final String website) {
      return new State(id, twitterAccount, linkedInAccount, website);
    }

    private State(final String id, final String twitterAccount, final String linkedInAccount, final String website) {
      this.id = id;
      this.twitterAccount = twitterAccount;
      this.linkedInAccount = linkedInAccount;
      this.website = website;
    }
  }
}
