package io.vlingo.examples.ecommerce.infra;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class ProductQuantityChangedEventAdapter implements EntryAdapter<CartEvents.ProductQuantityChangeEvent,Entry.TextEntry> {

    @Override
    public CartEvents.ProductQuantityChangeEvent fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, CartEvents.ProductQuantityChangeEvent.class);
    }

    @Override
    public Entry.TextEntry toEntry(final CartEvents.ProductQuantityChangeEvent source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(CartEvents.ProductQuantityChangeEvent.class, 1, serialization, Metadata.nullMetadata());
    }
}
