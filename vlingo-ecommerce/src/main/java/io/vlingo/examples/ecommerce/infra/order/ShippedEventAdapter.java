package io.vlingo.examples.ecommerce.infra.order;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.OrderEvents;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class ShippedEventAdapter implements EntryAdapter<OrderEvents.OrderShipped,BaseEntry.TextEntry> {

    @Override
    public OrderEvents.OrderShipped fromEntry(final BaseEntry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), OrderEvents.OrderShipped.class);
    }

    @Override
    public BaseEntry.TextEntry toEntry(final OrderEvents.OrderShipped source, final Metadata metadata) {
        return toEntry(source, source.orderId, metadata);
    }
    
    @Override
    public BaseEntry.TextEntry toEntry(final OrderEvents.OrderShipped source, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new BaseEntry.TextEntry(id, OrderEvents.OrderShipped.class, 1, serialization, metadata);
    }
}
