package io.vlingo.examples.ecommerce.infra.order;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.OrderEvents;
import io.vlingo.examples.ecommerce.model.OrderEvents.Created;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class OrderCreatedEventAdapter implements EntryAdapter<OrderEvents.Created,BaseEntry.TextEntry> {

    @Override
    public OrderEvents.Created fromEntry(final BaseEntry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), OrderEvents.Created.class);
    }

    @Override
    public BaseEntry.TextEntry toEntry(final OrderEvents.Created source) {
        return toEntry(source, source.orderId);
    }

    @Override
    public TextEntry toEntry(final Created source, final String id) {
        final String serialization = JsonSerialization.serialized(source);
        return new BaseEntry.TextEntry(id, OrderEvents.Created.class, 1, serialization, Metadata.nullMetadata());
    }
}
