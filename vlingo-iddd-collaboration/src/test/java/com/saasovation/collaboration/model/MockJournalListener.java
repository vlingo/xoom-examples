// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.journal.JournalListener;

public class MockJournalListener implements JournalListener<String> {
  private final List<Entry<String>> allEntries = new CopyOnWriteArrayList<>();
  private final List<State<String>> allSnapshots = new CopyOnWriteArrayList<>();
  private final AtomicReference<Integer> confirmedCount = new AtomicReference<>(0);
  private TestUntil until = TestUntil.happenings(0);

  @Override
  public synchronized void appended(final Entry<String> entry) {
    allEntries.add(entry);
    allSnapshots.add(State.TextState.Null);
    until.happened();
  }

  @Override
  public synchronized void appendedWith(final Entry<String> entry, final State<String> snapshot) {
    allEntries.add(entry);
    allSnapshots.add(snapshot);
    until.happened();
  }

  @Override
  public synchronized void appendedAll(final List<Entry<String>> entries) {
    allEntries.addAll(entries);
    for (int idx = 0; idx < entries.size(); ++idx) {
      allSnapshots.add(State.TextState.Null);
    }
    until.happened();
  }

  @Override
  public synchronized void appendedAllWith(final List<Entry<String>> entries, final State<String> snapshot) {
    allEntries.addAll(entries);
    for (int idx = 0; idx < (entries.size() - 1); ++idx) {
      allSnapshots.add(State.TextState.Null);
    }
    allSnapshots.add(snapshot == null ? State.TextState.Null : snapshot);
    until.happened();
  }

  public synchronized int confirmedCount() {
    return confirmedCount.get();
  }

  public synchronized void confirmExpectedEntries(final int count, final int retries) {
    for (int idx = 0; idx < retries; ++idx) {
      until.completesWithin(100L);
      final int peekCount = allEntries.size();
      if (peekCount == count) {
        confirmedCount.set(peekCount);
        break;
      }
    }

    if (confirmedCount.get() != count) {
      final int peekCount = allEntries.size();
      confirmedCount.set(peekCount);
    }
    until.happened();
  }

  public synchronized Entry<String> entry(final int index) {
    return allEntries.get(index);
  }

  public synchronized TestUntil until(final int times) {
    until.resetHappeningsTo(times);
    return until;
  }
}
