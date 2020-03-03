// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import io.vlingo.common.Completes;

public interface Profile {
  Completes<Profile.ProfileState> withTwitterAccount(final String twitterAccount);
  Completes<Profile.ProfileState> withLinkedInAccount(final String linkedInAccount);
  Completes<Profile.ProfileState> withWebSite(final String website);

  public static ProfileState from(final String id, final String twitterAccount, final String linkedInAccount, final String website) {
    return new ProfileState(id, twitterAccount, linkedInAccount, website);
  }

  public static ProfileState nonExisting() {
    return new ProfileState(null, null, null, null);
  }

  public static final class ProfileState {

    public static ProfileState of(String id) {
      return new ProfileState(id, null, null, null);
    }

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

    ProfileState withTwitterAccount(final String twitterAccount) {
      return new ProfileState(id, twitterAccount, linkedInAccount, website);
    }

    ProfileState withLinkedInAccount(final String linkedInAccount) {
      return new ProfileState(id, twitterAccount, linkedInAccount, website);
    }

    ProfileState withWebSite(final String website) {
      return new ProfileState(id, twitterAccount, linkedInAccount, website);
    }

    private ProfileState(final String id, final String twitterAccount, final String linkedInAccount, final String website) {
      this.id = id;
      this.twitterAccount = twitterAccount;
      this.linkedInAccount = linkedInAccount;
      this.website = website;
    }
  }
}
