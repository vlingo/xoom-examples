package io.vlingo.examples.ecommerce.infra.cart;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.examples.ecommerce.model.CartEvents.CreatedForUser;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.Entry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class CartCreatedEventAdapter implements EntryAdapter<CartEvents.CreatedForUser,Entry.TextEntry> {

    @Override
    public CartEvents.CreatedForUser fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, CartEvents.CreatedForUser.class);
    }

    @Override
    public Entry.TextEntry toEntry(final CartEvents.CreatedForUser source) {
      return toEntry(source, source.cartId);
    }

    @Override
    public TextEntry toEntry(CreatedForUser source, String id) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(id, CartEvents.CreatedForUser.class, 1, serialization, Metadata.nullMetadata());
    }
}
