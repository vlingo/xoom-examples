package io.vlingo.examples.ecommerce.infra.cart;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.examples.ecommerce.model.CartEvents.CreatedForUser;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class CartCreatedEventAdapter implements EntryAdapter<CartEvents.CreatedForUser,BaseEntry.TextEntry> {

    @Override
    public CartEvents.CreatedForUser fromEntry(final BaseEntry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), CartEvents.CreatedForUser.class);
    }

    @Override
    public TextEntry toEntry(final CreatedForUser source, final Metadata metadata) {
        return toEntry(source, source.cartId, metadata);
    }

    @Override
    public TextEntry toEntry(final CreatedForUser source, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new BaseEntry.TextEntry(id, CartEvents.CreatedForUser.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final CreatedForUser source, final int version, final String id, final Metadata metadata) {
      final String serialization = JsonSerialization.serialized(source);
      return new BaseEntry.TextEntry(id, CartEvents.CreatedForUser.class, 1, serialization, version, metadata);
    }
}
