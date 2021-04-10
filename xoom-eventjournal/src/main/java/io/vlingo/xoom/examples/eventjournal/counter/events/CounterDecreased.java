package io.vlingo.xoom.examples.eventjournal.counter.events;

import java.util.UUID;

public class CounterDecreased extends Event {
    public final UUID uuid;

    public CounterDecreased(int currentCounter) {
        super(currentCounter);
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean isDecreased() {
      return true;
    }
}
