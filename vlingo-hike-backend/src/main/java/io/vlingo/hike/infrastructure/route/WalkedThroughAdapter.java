package io.vlingo.hike.infrastructure.route;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.hike.domain.route.events.WalkedThrough;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class WalkedThroughAdapter implements EntryAdapter<WalkedThrough, Entry.TextEntry> {
    public static WalkedThroughAdapter instance() {
        return new WalkedThroughAdapter();
    }

    @Override
    public WalkedThrough fromEntry(Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, WalkedThrough.class);
    }

    @Override
    public Entry.TextEntry toEntry(WalkedThrough source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(WalkedThrough.class, 1, serialization, Metadata.nullMetadata());
    }
}
