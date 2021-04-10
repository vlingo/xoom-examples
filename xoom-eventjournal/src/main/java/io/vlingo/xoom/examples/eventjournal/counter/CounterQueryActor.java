// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.eventjournal.counter;

import java.util.Optional;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.common.Cancellable;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.common.Scheduled;
import io.vlingo.xoom.examples.eventjournal.counter.events.Event;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.symbio.BaseEntry.TextEntry;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.journal.JournalReader;

public class CounterQueryActor extends Actor implements CounterQuery, Scheduled<Object> {
    private final JournalReader<TextEntry> streamReader;
    private final Cancellable cancellable;
    private Event counted;
    private Optional<Integer> currentCount;
    private EntryAdapterProvider entryAdapterProvider;

    @SuppressWarnings("unchecked")
    public CounterQueryActor(JournalReader<TextEntry> streamReader, EntryAdapterProvider entryAdapterProvider) {
        this.streamReader = streamReader;
        this.entryAdapterProvider = entryAdapterProvider;
        this.cancellable = scheduler().schedule(selfAs(Scheduled.class), null, 0, 5);
        this.currentCount = Optional.empty();
        intervalSignal(null, null);
    }

    @Override
    public Completes<Integer> counter() {
        return completes().with(currentCount.orElse(-1));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void intervalSignal(Scheduled<Object> scheduled, Object data) {
      streamReader.readNext()
        .andThen(event -> ((BaseEntry) event).asTextEntry())
        .andThenConsume(entry -> {
          counted = (Event) entryAdapterProvider.asSource(entry);
          currentCount = Optional.of(counted.currentCounter);
        });
    }

    @Override
    public void stop() {
        cancellable.cancel();
    }
}
