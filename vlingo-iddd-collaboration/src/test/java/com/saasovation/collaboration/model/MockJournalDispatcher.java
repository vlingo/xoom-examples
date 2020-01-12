// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.common.Tuple2;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MockJournalDispatcher implements Dispatcher<Dispatchable<Entry<String>, State<String>>> {
  private AccessSafely access = afterCompleting(1);

  private final List<Entry<String>> allEntries = new ArrayList<>();
  private final List<State<String>> allSnapshots = new ArrayList<>();
  
  @Override
  public void controlWith(final DispatcherControl control) {

  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State<String>> dispatchable) {
    access.writeUsing("entries", Tuple2.from(dispatchable.entries(), snapshots(dispatchable.entries().size(), dispatchable.typedState())));
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
