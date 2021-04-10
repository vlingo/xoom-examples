package io.vlingo.xoom.examples.eventjournal.counter.events;

import java.util.UUID;

public class CounterIncreased extends Event {
    public final UUID uuid;

    public CounterIncreased(int currentCounter) {
        super(currentCounter);
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean isIncreased() {
      return true;
    }
}
