// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.eventjournal.counter;

import java.util.Optional;

import io.vlingo.actors.Actor;
import io.vlingo.common.Cancellable;
import io.vlingo.common.Completes;
import io.vlingo.common.Scheduled;
import io.vlingo.eventjournal.counter.events.Event;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.journal.JournalReader;

public class CounterQueryActor extends Actor implements CounterQuery, Scheduled {
    private final JournalReader<String> streamReader;
    private final Cancellable cancellable;
    private Event counted;
    private Optional<Integer> currentCount;
    private EntryAdapterProvider entryAdapterProvider;

    public CounterQueryActor(JournalReader<String> streamReader, EntryAdapterProvider entryAdapterProvider) {
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
    public void intervalSignal(Scheduled scheduled, Object data) {
      streamReader.readNext()
        .andThen(event -> event.asTextEntry())
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
