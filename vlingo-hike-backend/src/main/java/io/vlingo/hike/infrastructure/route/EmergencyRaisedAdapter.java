package io.vlingo.hike.infrastructure.route;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.hike.domain.route.events.EmergencyRaised;
import io.vlingo.hike.domain.route.events.WalkedThrough;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class EmergencyRaisedAdapter implements EntryAdapter<EmergencyRaised, Entry.TextEntry> {
    public static EmergencyRaisedAdapter instance() {
        return new EmergencyRaisedAdapter();
    }

    @Override
    public EmergencyRaised fromEntry(Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, EmergencyRaised.class);
    }

    @Override
    public Entry.TextEntry toEntry(EmergencyRaised source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(EmergencyRaised.class, 1, serialization, Metadata.nullMetadata());
    }
}
