// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import io.vlingo.frontservice.data.UserData;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.User;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;

public class UserProjectionActor extends StateStoreProjectionActor<UserData> {
  private String becauseOf;
  
  public UserProjectionActor() {
    super(QueryModelStoreProvider.instance().store);
  }

  @Override
  protected UserData currentDataFor(final Projectable projectable) {
    becauseOf = projectable.becauseOf()[0];
    
    final User.UserState state = projectable.object();
    final UserData current = UserData.from(state);
    
    return current;
  }

  @Override
  protected UserData merge(final UserData previousData, final int previousVersion, final UserData currentData, final int currentVersion) {
    UserData merged;
    
    switch (becauseOf) {
    case "User:new":
      merged = currentData;
      break;
    case "User:contact":
      merged = UserData.from(previousData.id, previousData.nameData, currentData.contactData, previousData.publicSecurityToken);
      break;
    case "User:name":
      merged = UserData.from(previousData.id, currentData.nameData, previousData.contactData, previousData.publicSecurityToken);
      break;
    default:
      merged = currentData;
    }

    return merged;
  }
}
