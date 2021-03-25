// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.developers.petclinic;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MockJournalDispatcher implements Dispatcher<Dispatchable<Entry<String>, State<?>>> {

  private AccessSafely access;

  private List<Entry<String>> entries = new CopyOnWriteArrayList<>();

  public MockJournalDispatcher() {
    super();
    this.access = afterCompleting(0);
  }

  public AccessSafely afterCompleting(final int times) {
    access = AccessSafely
      .afterCompleting(times)

      .writingWith("appended", (Entry<String> appended) -> entries.add(appended))
      .writingWith("appendedAll", (List<Entry<String>> appended) -> entries.addAll(appended))
      .readingWith("appendedAt", (Integer index) -> entries.get(index))

      .readingWith("entries", () -> entries)
      .readingWith("entriesCount", () -> entries.size());

    return access;
  }

  @Override
  public void controlWith(final DispatcherControl control) {

  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State<?>> dispatchable) {
    access.writeUsing("appendedAll", dispatchable.entries());
  }
}
