package io.vlingo.examples.ecommerce.infra.cart;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.examples.ecommerce.model.CartEvents.AllItemsRemovedEvent;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.Entry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class CartAllItemsRemoveEventAdapter implements EntryAdapter<CartEvents.AllItemsRemovedEvent,Entry.TextEntry> {

    @Override
    public CartEvents.AllItemsRemovedEvent fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, CartEvents.AllItemsRemovedEvent.class);
    }

    @Override
    public Entry.TextEntry toEntry(final CartEvents.AllItemsRemovedEvent source) {
        return toEntry(source, source.cartId);
    }

    @Override
    public TextEntry toEntry(final AllItemsRemovedEvent source, final String id) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(id, CartEvents.AllItemsRemovedEvent.class, 1, serialization, Metadata.nullMetadata());
    }
}
