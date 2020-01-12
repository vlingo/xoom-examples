// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import io.vlingo.actors.Actor;
import io.vlingo.common.Outcome;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.User;
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

public class UserProjectionActor extends Actor
    implements Projection, ReadResultInterest, WriteResultInterest {

  private final ReadResultInterest readInterest;
  private final WriteResultInterest writeInterest;
  private final StateStore store;

  public UserProjectionActor() {
    this.store = QueryModelStoreProvider.instance().store;
    this.readInterest = selfAs(ReadResultInterest.class);
    this.writeInterest = selfAs(WriteResultInterest.class);
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    final User.UserState state = projectable.object();
    final UserData current = UserData.from(state);

    switch (projectable.becauseOf()[0]) {
      case "User:new": {
        store.write(state.id, current, 1, writeInterest, control.confirmerFor(projectable));
        break;
      }
      case "User:contact": {
        final Consumer<UserData> updater = previous -> {
          updateWith(previous, current, state.version,
            (writeData) -> UserData.from(writeData.id, writeData.nameData, current.contactData, writeData.publicSecurityToken),
            control.confirmerFor(projectable)
          );
        };
        store.read(current.id, UserData.class, readInterest, updater);
        break;
      }
      case "User:name": {
        final Consumer<UserData> updater = previous -> {
          updateWith(previous, current, state.version,
            (writeData) -> UserData.from(writeData.id, current.nameData, writeData.contactData, writeData.publicSecurityToken),
            control.confirmerFor(projectable)
          );
        };
        store.read(current.id, UserData.class, readInterest, updater);
        break;
      }
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

  private void updateWith(final UserData previous, final UserData current, final int version, final Function<UserData,UserData> updater, final Confirmer confirmer) {
    final UserData data = updater.apply(previous);
    store.write(data.id, data, version, writeInterest, confirmer);
  }
}
