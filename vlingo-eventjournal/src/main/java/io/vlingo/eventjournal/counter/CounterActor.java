package io.vlingo.eventjournal.counter;

import io.vlingo.actors.Actor;
import io.vlingo.eventjournal.counter.events.CounterDecreased;
import io.vlingo.eventjournal.counter.events.CounterDecreasedAdapter;
import io.vlingo.eventjournal.counter.events.CounterIncreased;
import io.vlingo.eventjournal.counter.events.CounterIncreasedAdapter;
import io.vlingo.symbio.store.eventjournal.EventJournal;

public class CounterActor extends Actor implements Counter {
    private final String counterName;
    private final EventJournal<String> journal;
    private final CounterIncreasedAdapter counterIncreasedAdapter;
    private final CounterDecreasedAdapter counterDecreasedAdapter;
    private int currentCount;
    private int version;

    public CounterActor(final String counterName, final EventJournal<String> journal) {
        this.counterName = counterName;
        this.journal = journal;
        this.currentCount = 0;
        this.version = 1;
        this.counterIncreasedAdapter = new CounterIncreasedAdapter();
        this.counterDecreasedAdapter = new CounterDecreasedAdapter();
    }

    @Override
    public void increase() {
        currentCount++;
        journal.append(counterName, version++, counterIncreasedAdapter.serialize(new CounterIncreased(currentCount)));
    }

    @Override
    public void decrease() {
        currentCount--;
        journal.append(counterName, version++, counterDecreasedAdapter.serialize(new CounterDecreased(currentCount)));
    }
}
