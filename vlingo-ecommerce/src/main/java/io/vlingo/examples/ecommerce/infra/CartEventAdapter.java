package io.vlingo.examples.ecommerce.infra;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class CartEventAdapter implements EntryAdapter<CartEvents.CreatedEvent,Entry.TextEntry> {

    @Override
    public CartEvents.CreatedEvent fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, CartEvents.CreatedEvent.class);
    }

    @Override
    public Entry.TextEntry toEntry(final CartEvents.CreatedEvent source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(CartEvents.CreatedEvent.class, 1, serialization, Metadata.nullMetadata());
    }
}
