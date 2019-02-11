// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.common.Tuple2;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.journal.JournalListener;

public class MockJournalListener implements JournalListener<String> {
  private AccessSafely access = afterCompleting(1);

  private final List<Entry<String>> allEntries = new ArrayList<>();
  private final List<State<String>> allSnapshots = new ArrayList<>();

  @Override
  public void appended(final Entry<String> entry) {
    access.writeUsing("entry", Tuple2.from(entry, State.TextState.Null));
  }

  @Override
  public void appendedWith(final Entry<String> entry, final State<String> snapshot) {
    access.writeUsing("entry", Tuple2.from(entry, snapshot));
  }

  @Override
  public void appendedAll(final List<Entry<String>> entries) {
    access.writeUsing("entries", Tuple2.from(entries, snapshots(entries.size(), State.TextState.Null)));
  }

  @Override
  public void appendedAllWith(final List<Entry<String>> entries, final State<String> snapshot) {
    access.writeUsing("entries", Tuple2.from(entries, snapshots(entries.size(), snapshot)));
  }

  public int confirmedCount() {
    final int count = access.readFrom("count");
    return count;
  }

  public int confirmedCount(final int expected) {
    final int count = access.readFromExpecting("count", expected);
    return count;
  }

  public Entry<String> entry(final int index) {
    final Entry<String> entry = access.readFrom("entry", index);
    return entry;
  }

  public Entry<String> snapshot(final int index) {
    final Entry<String> entry = access.readFrom("entry", index);
    return entry;
  }

  public AccessSafely afterCompleting(final int times) {
    access =
            AccessSafely
              .afterCompleting(times)
              .writingWith("entry",
                      (Tuple2<Entry<String>,State<String>> entry) -> {
                        allEntries.add(entry._1);
                        allSnapshots.add(entry._2);
              })
              .writingWith("entries",
                      (Tuple2<Collection<Entry<String>>,Collection<State<String>>> entries) -> {
                        allEntries.addAll(entries._1);
                        allSnapshots.addAll(entries._2);
              })
              .readingWith("count", () -> allEntries.size())
              .readingWith("snapshot", (Integer index) -> allSnapshots.get(index))
              .readingWith("entry", (Integer index) -> allEntries.get(index));

    return access;
  }

  private List<State<String>> snapshots(final int count, final State<String> snapshot) {
    final State<String>[] array = new State.TextState[count];
    Arrays.fill(array, State.TextState.Null);
    array[array.length - 1] = (snapshot == null ? State.TextState.Null : snapshot);
    final List<State<String>> snapshots = Arrays.asList(array);
    return snapshots;
  }
}
