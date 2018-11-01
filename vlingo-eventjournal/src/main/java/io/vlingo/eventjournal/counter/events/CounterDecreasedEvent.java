package io.vlingo.eventjournal.counter.events;

import com.google.gson.Gson;
import io.vlingo.symbio.Event;
import io.vlingo.symbio.Metadata;

import java.util.UUID;

public class CounterDecreasedEvent {
    private final UUID uuid;
    public final int currentCounter;

    public CounterDecreasedEvent(int currentCounter) {
        this.uuid = UUID.randomUUID();
        this.currentCounter = currentCounter;
    }

    public Event<String> toTextEvent() {
        return new Event.TextEvent(uuid.toString(), getClass(), 1, new Gson().toJson(this), new Metadata());
    }
}
