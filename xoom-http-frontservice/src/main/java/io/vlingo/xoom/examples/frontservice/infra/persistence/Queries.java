// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.infra.persistence;

import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.frontservice.data.ProfileData;
import io.vlingo.xoom.examples.frontservice.data.UserData;

public interface Queries {
  Completes<ProfileData> profileOf(String userId);
  Completes<UserData> userDataOf(String userId);
  Completes<Collection<UserData>> usersData();
}
