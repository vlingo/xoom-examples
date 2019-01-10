package io.vlingo.examples.ecommerce.infra;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class AllItemsRemoveEventAdapter implements EntryAdapter<CartEvents.AllItemsRemovedEvent,Entry.TextEntry> {

    @Override
    public CartEvents.AllItemsRemovedEvent fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, CartEvents.AllItemsRemovedEvent.class);
    }

    @Override
    public Entry.TextEntry toEntry(final CartEvents.AllItemsRemovedEvent source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(CartEvents.AllItemsRemovedEvent.class, 1, serialization, Metadata.nullMetadata());
    }
}
