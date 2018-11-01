package io.vlingo.eventjournal.counter;

import io.vlingo.actors.Actor;
import io.vlingo.eventjournal.counter.events.CounterDecreasedEvent;
import io.vlingo.eventjournal.counter.events.CounterIncreasedEvent;
import io.vlingo.symbio.Event;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.eventjournal.EventJournal;

import java.util.UUID;

public class CounterActor extends Actor implements Counter {
    private final String counterName;
    private final EventJournal<String> journal;
    private int currentCount;
    private int version;

    public CounterActor(final String counterName, final EventJournal<String> journal) {
        this.counterName = counterName;
        this.journal = journal;
        this.currentCount = 0;
        this.version = 1;
    }

    @Override
    public void increase() {
        currentCount++;
        journal.append(counterName, version++, new CounterIncreasedEvent(currentCount).toTextEvent());
    }

    @Override
    public void decrease() {
        currentCount--;
        journal.append(counterName, version++, new CounterDecreasedEvent(currentCount).toTextEvent());
    }
}
