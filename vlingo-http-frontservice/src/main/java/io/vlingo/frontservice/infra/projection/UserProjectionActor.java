// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import java.util.function.Consumer;
import java.util.function.Function;

import io.vlingo.actors.Actor;
import io.vlingo.common.Outcome;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.infra.persistence.UserDataStateAdapter;
import io.vlingo.frontservice.model.User;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.Projection;
import io.vlingo.lattice.model.projection.ProjectionControl;
import io.vlingo.lattice.model.projection.ProjectionControl.Confirmer;
import io.vlingo.symbio.State;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore.ReadResultInterest;
import io.vlingo.symbio.store.state.StateStore.WriteResultInterest;
import io.vlingo.symbio.store.state.TextStateStore;

public class UserProjectionActor extends Actor
    implements Projection, ReadResultInterest<TextState>, WriteResultInterest<TextState> {

  private final UserDataStateAdapter adapter;
  private final ReadResultInterest<TextState> readInterest;
  private final WriteResultInterest<TextState> writeInterest;
  private final TextStateStore store;

  @SuppressWarnings("unchecked")
  public UserProjectionActor() {
    this.store = QueryModelStoreProvider.instance().store;
    this.adapter = new UserDataStateAdapter();
    this.readInterest = selfAs(ReadResultInterest.class);
    this.writeInterest = selfAs(WriteResultInterest.class);
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    final User.UserState state = projectable.object();
    final UserData data = UserData.from(state);

    switch (projectable.becauseOf()) {
      case "User:new": {
        final TextState projection = adapter.toRawState(data, 1);
        store.write(projection, writeInterest, control.confirmerFor(projectable));
        break;
      }
      case "User:contact": {
        final Consumer<TextState> updater = readState -> {
          updateWith(readState, data, state.version,
            (writeData) -> UserData.from(writeData.id, writeData.nameData, data.contactData, writeData.publicSecurityToken),
            control.confirmerFor(projectable)
          );
        };
        store.read(data.id, UserData.class, readInterest, updater);
        break;
      }
      case "User:name": {
        final Consumer<TextState> updater = readState -> {
          updateWith(readState, data, state.version,
            (writeData) -> UserData.from(writeData.id, data.nameData, writeData.contactData, writeData.publicSecurityToken),
            control.confirmerFor(projectable)
          );
        };
        store.read(data.id, UserData.class, readInterest, updater);
        break;
      }
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void readResultedIn(final Outcome<StorageException, Result> outcome, final String id, final TextState state, final Object object) {
    outcome.andThen(result -> {
      ((Consumer<State<String>>) object).accept(state);
      return result;
    }).otherwise(cause -> {
      // log but don't retry, allowing re-delivery of Projectable
      logger().log("Query state not read for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }

  @Override
  public void writeResultedIn(final Outcome<StorageException, Result> outcome, final String id, final TextState state, final Object object) {
    outcome.andThen(result -> {
      ((Confirmer) object).confirm();
      return result;
    }).otherwise(cause -> {
      // log but don't retry, allowing re-delivery of Projectable
      logger().log("Query state not written for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }

  private void updateWith(final TextState state, final UserData data, final int version, final Function<UserData,UserData> updater, final Confirmer confirmer) {
    final UserData read = adapter.fromRawState(state);
    final UserData write = updater.apply(read);
    final TextState projection = adapter.toRawState(write, 1);
    store.write(projection, writeInterest, confirmer);
  }
}
