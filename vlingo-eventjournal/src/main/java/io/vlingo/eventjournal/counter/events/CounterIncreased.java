package io.vlingo.eventjournal.counter.events;

import java.util.UUID;

public class CounterIncreased {
    public final UUID uuid;
    public final int currentCounter;

    public CounterIncreased(int currentCounter) {
        this.uuid = UUID.randomUUID();
        this.currentCounter = currentCounter;
    }
}
