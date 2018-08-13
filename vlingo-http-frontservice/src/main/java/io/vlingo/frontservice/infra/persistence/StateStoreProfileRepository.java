// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import java.util.HashMap;
import java.util.Map;

import io.vlingo.frontservice.model.Profile;
import io.vlingo.frontservice.model.ProfileRepository;

public class StateStoreProfileRepository implements ProfileRepository {
  private static ProfileRepository instance;
  
  private final Map<String,Profile.State> profiles;

  public static synchronized ProfileRepository instance() {
    if (instance == null) {
      instance = new StateStoreProfileRepository();
    }
    return instance;
  }
  
  public Profile.State profileOf(final String userId) {
    final Profile.State profileState = profiles.get(userId);
    
    return profileState == null ? Profile.nonExisting() : profileState;
  }

  public void save(final Profile.State profileState) {
    profiles.put(profileState.id, profileState);
  }

  private StateStoreProfileRepository() {
    this.profiles = new HashMap<>();
  }
}
