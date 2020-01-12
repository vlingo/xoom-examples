// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.persistence;

import java.util.Collection;
import java.util.function.BiConsumer;

import io.vlingo.actors.Actor;
import io.vlingo.actors.CompletesEventually;
import io.vlingo.common.Completes;
import io.vlingo.common.Outcome;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateStore.ReadResultInterest;

public class QueriesActor extends Actor implements Queries, ReadResultInterest {
  private final ReadResultInterest interest;
  private final StateStore store;

  public QueriesActor(final StateStore store) {
    this.store = store;
    this.interest = selfAs(ReadResultInterest.class);
  }

  @Override
  public Completes<ProfileData> profileOf(final String userId) {
    final CompletesEventually completesEventually = completesEventually();
    final BiConsumer<ProfileData,Integer> translator = (data, version) -> {
      if (data != null) {
        completesEventually.with(data);
      } else {
        completesEventually.with(ProfileData.empty());
      }
    };
    store.read(userId, ProfileData.class, interest, translator);

    return completes(); // unanswered until preserved
  }

  @Override
  public Completes<UserData> userDataOf(final String userId) {
    final CompletesEventually completesEventually = completesEventually();
    final BiConsumer<UserData,Integer> translator = (data, version) -> {
      if (data != null) {
        completesEventually.with(data);
      } else {
        completesEventually.with(UserData.empty());
      }
    };
    store.read(userId, UserData.class, interest, translator);

    return completes(); // unanswered until preserved
  }

  @Override
  public Completes<Collection<UserData>> usersData() {
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> void readResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final Metadata metadata, final Object object) {
    outcome.andThen(result -> {
      ((BiConsumer<S,Integer>) object).accept(state, stateVersion);
      return result;
    }).otherwise(cause -> {
      ((BiConsumer<S,Integer>) object).accept(null, -1);
      return cause.result;
    });
  }
}
