// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import java.util.List;
import java.util.function.Consumer;

import io.vlingo.actors.Actor;
import io.vlingo.common.Outcome;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.Profile;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.Projection;
import io.vlingo.lattice.model.projection.ProjectionControl;
import io.vlingo.lattice.model.projection.ProjectionControl.Confirmer;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateStore.ReadResultInterest;
import io.vlingo.symbio.store.state.StateStore.WriteResultInterest;

public class ProfileProjectionActor extends Actor
    implements Projection, ReadResultInterest, WriteResultInterest {

  // TODO: for you to complete the implementation

  //private final ReadResultInterest<String> readInterest;
  private final WriteResultInterest writeInterest;
  private final StateStore store;

  public ProfileProjectionActor() {
    this.store = QueryModelStoreProvider.instance().store;
    //this.readInterest = selfAs(ReadResultInterest.class);
    this.writeInterest = selfAs(WriteResultInterest.class);
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    final Profile.ProfileState state = projectable.object();
    final ProfileData data = ProfileData.from(state);

    switch (projectable.becauseOf()) {
    case "Profile:new": {
      store.write(state.id, data, 1, writeInterest, control.confirmerFor(projectable));
      break;
    }
    case "Profile:twitter":
      break;
    case "Profile:linkedIn":
      break;
    case "Profile:website":
      break;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> void readResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final Metadata metadata, final Object object) {
    outcome.andThen(result -> {
      ((Consumer<S>) object).accept(state);
      return result;
    }).otherwise(cause -> {
      // log but don't retry, allowing re-delivery of Projectable
      logger().info("Query state not read for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }

  @Override
  public <S,C> void writeResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final List<Source<C>> sources, final Object object) {
    outcome.andThen(result -> {
      ((Confirmer) object).confirm();
      return result;
    }).otherwise(cause -> {
      // log but don't retry, allowing re-delivery of Projectable
      logger().info("Query state not written for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }
}
