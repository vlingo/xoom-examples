// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.data.UserData;

public interface Queries {
  Completes<ProfileData> profileOf(String userId);
  Completes<UserData> userDataOf(String userId);
  Completes<Collection<UserData>> usersData();
}
