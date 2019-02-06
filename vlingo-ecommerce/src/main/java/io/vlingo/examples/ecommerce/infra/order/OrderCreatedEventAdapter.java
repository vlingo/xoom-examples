package io.vlingo.examples.ecommerce.infra.order;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.examples.ecommerce.model.OrderEvents;
import io.vlingo.examples.ecommerce.model.OrderEvents.Created;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.Entry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class OrderCreatedEventAdapter implements EntryAdapter<OrderEvents.Created,Entry.TextEntry> {

    @Override
    public OrderEvents.Created fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, OrderEvents.Created.class);
    }

    @Override
    public Entry.TextEntry toEntry(final OrderEvents.Created source) {
        return toEntry(source, source.orderId);
    }

    @Override
    public TextEntry toEntry(final Created source, final String id) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(id, CartEvents.CreatedForUser.class, 1, serialization, Metadata.nullMetadata());
    }
}
