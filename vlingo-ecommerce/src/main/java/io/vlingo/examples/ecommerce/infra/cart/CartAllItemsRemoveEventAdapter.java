package io.vlingo.examples.ecommerce.infra.cart;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.examples.ecommerce.model.CartEvents.AllItemsRemovedEvent;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class CartAllItemsRemoveEventAdapter implements EntryAdapter<CartEvents.AllItemsRemovedEvent,BaseEntry.TextEntry> {

    @Override
    public CartEvents.AllItemsRemovedEvent fromEntry(final BaseEntry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), CartEvents.AllItemsRemovedEvent.class);
    }

    @Override
    public BaseEntry.TextEntry toEntry(final CartEvents.AllItemsRemovedEvent source) {
        return toEntry(source, source.cartId);
    }

    @Override
    public TextEntry toEntry(final AllItemsRemovedEvent source, final String id) {
        final String serialization = JsonSerialization.serialized(source);
        return new BaseEntry.TextEntry(id, CartEvents.AllItemsRemovedEvent.class, 1, serialization, Metadata.nullMetadata());
    }
}
