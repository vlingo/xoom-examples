package io.vlingo.eventjournal.counter.events;

import com.google.gson.Gson;
import io.vlingo.eventjournal.events.EventAdapter;
import io.vlingo.symbio.Event;
import io.vlingo.symbio.Metadata;


public class CounterDecreasedAdapter implements EventAdapter<CounterDecreased> {
    private final Gson gson;

    public CounterDecreasedAdapter() {
        this.gson = new Gson();
    }

    @Override
    public Event.TextEvent serialize(CounterDecreased event) {
        return new Event.TextEvent(event.uuid.toString(), CounterDecreased.class, 1, gson.toJson(event), new Metadata());

    }

    @Override
    public boolean canDeserialize(Event.TextEvent event) {
        return event.type.equals(CounterDecreased.class.getCanonicalName()) && event.typeVersion == 1;
    }

    @Override
    public CounterDecreased deserialize(Event.TextEvent event) {
        return gson.fromJson(event.eventData, CounterDecreased.class);
    }
}
