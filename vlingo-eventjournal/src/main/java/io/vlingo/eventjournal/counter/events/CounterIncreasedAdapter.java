package io.vlingo.eventjournal.counter.events;

import com.google.gson.Gson;
import io.vlingo.eventjournal.events.EventAdapter;
import io.vlingo.symbio.Event;
import io.vlingo.symbio.Metadata;


public class CounterIncreasedAdapter implements EventAdapter<CounterIncreased> {
    private final Gson gson;

    public CounterIncreasedAdapter() {
        this.gson = new Gson();
    }

    @Override
    public Event.TextEvent serialize(CounterIncreased event) {
        return new Event.TextEvent(event.uuid.toString(), CounterIncreased.class, 1, gson.toJson(event), new Metadata());

    }

    @Override
    public boolean canDeserialize(Event.TextEvent event) {
        return event.type.equals(CounterIncreased.class.getCanonicalName()) && event.typeVersion == 1;
    }

    @Override
    public CounterIncreased deserialize(Event.TextEvent event) {
        return gson.fromJson(event.eventData, CounterIncreased.class);
    }
}
