package io.vlingo.examples.ecommerce.infra;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class ProductAddedEventAdapter implements EntryAdapter<CartEvents.ProductAddedEvent,Entry.TextEntry> {

    @Override
    public CartEvents.ProductAddedEvent fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, CartEvents.ProductAddedEvent.class);
    }

    @Override
    public Entry.TextEntry toEntry(final CartEvents.ProductAddedEvent source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(CartEvents.ProductAddedEvent.class, 1, serialization, Metadata.nullMetadata());
    }
}
