// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;

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
