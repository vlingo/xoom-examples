// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.data;

import io.vlingo.frontservice.model.Profile;

public class ProfileData {
  public final String id;
  public final String linkedInAccount;
  public final String twitterAccount;
  public final String website;

  public static ProfileData empty() {
    return new ProfileData(null, null, null, null);
  }

  public static ProfileData from(final Profile.ProfileState profile) {
    return new ProfileData(profile.id, profile.twitterAccount, profile.linkedInAccount, profile.website);
  }
  
  public static ProfileData from(final String id, final String twitterAccount, final String linkedInAccount, final String website) {
    return new ProfileData(id, twitterAccount, linkedInAccount, website);
  }
  
  public ProfileData(final String id, final String twitterAccount, final String linkedInAccount, final String website) {
    this.id = id;
    this.twitterAccount = twitterAccount;
    this.linkedInAccount = linkedInAccount;
    this.website = website;
  }

  public boolean doesNotExist() {
    return twitterAccount == null && linkedInAccount == null && website == null;
  }
}
