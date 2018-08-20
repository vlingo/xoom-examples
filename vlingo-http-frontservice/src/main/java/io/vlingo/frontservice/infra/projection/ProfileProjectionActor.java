// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import io.vlingo.actors.Actor;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.symbio.projection.Projectable;
import io.vlingo.symbio.projection.Projection;
import io.vlingo.symbio.projection.ProjectionControl;
import io.vlingo.symbio.store.state.TextStateStore;

public class ProfileProjectionActor extends Actor implements Projection {
  private final TextStateStore store;

  public ProfileProjectionActor() {
    this.store = QueryModelStoreProvider.instance().store;
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    switch (projectable.becauseOf()) {
    case "Profile:new":
      break;
    case "Profile:twitter":
      break;
    case "Profile:linkedIn":
      break;
    case "Profile:website":
      break;
    }

    control.confirmProjected(projectable.projectionId());
  }
}
