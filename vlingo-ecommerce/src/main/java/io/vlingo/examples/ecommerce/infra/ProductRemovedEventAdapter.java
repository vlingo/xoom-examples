package io.vlingo.examples.ecommerce.infra;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class ProductRemovedEventAdapter implements EntryAdapter<CartEvents.ProductRemovedEvent,Entry.TextEntry> {

    @Override
    public CartEvents.ProductRemovedEvent fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, CartEvents.ProductRemovedEvent.class);
    }

    @Override
    public Entry.TextEntry toEntry(final CartEvents.ProductRemovedEvent source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(CartEvents.ProductRemovedEvent.class, 1, serialization, Metadata.nullMetadata());
    }
}
