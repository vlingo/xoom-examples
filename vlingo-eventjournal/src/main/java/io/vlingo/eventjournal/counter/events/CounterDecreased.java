package io.vlingo.eventjournal.counter.events;

import java.util.UUID;

public class CounterDecreased {
    public final UUID uuid;
    public final int currentCounter;

    public CounterDecreased(int currentCounter) {
        this.uuid = UUID.randomUUID();
        this.currentCounter = currentCounter;
    }
}
