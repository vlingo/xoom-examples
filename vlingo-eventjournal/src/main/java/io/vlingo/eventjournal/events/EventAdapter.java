package io.vlingo.eventjournal.events;

import io.vlingo.symbio.Event;

public interface EventAdapter<EventType> {
    Event.TextEvent serialize(final EventType event);
    boolean canDeserialize(final Event.TextEvent event);
    EventType deserialize(final Event.TextEvent event);
}
