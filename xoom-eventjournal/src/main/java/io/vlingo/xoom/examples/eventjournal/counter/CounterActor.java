// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.eventjournal.counter;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.examples.eventjournal.MockCounterAppendResultInterest;
import io.vlingo.xoom.examples.eventjournal.counter.events.CounterDecreased;
import io.vlingo.xoom.examples.eventjournal.counter.events.CounterIncreased;
import io.vlingo.xoom.symbio.store.journal.Journal;

public class CounterActor extends Actor implements Counter {
    private final String counterName;
    private final Journal<String> journal;
    private int currentCount;
    private int version;

    public CounterActor(final String counterName, final Journal<String> journal) {
        this.counterName = counterName;
        this.journal = journal;
        this.currentCount = 0;
        this.version = 1;
    }

    @Override
    public void increase() {
        currentCount++;
        journal.append(counterName, version++, new CounterIncreased(currentCount), new MockCounterAppendResultInterest(), this);
    }

    @Override
    public void decrease() {
        currentCount--;
        journal.append(counterName, version++, new CounterDecreased(currentCount), new MockCounterAppendResultInterest(), this);
    }
}
