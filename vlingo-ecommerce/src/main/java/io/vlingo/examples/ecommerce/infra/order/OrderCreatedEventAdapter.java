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
    public TextEntry toEntry(final Created source, final Metadata metadata) {
        return toEntry(source, source.orderId, metadata);
    }
    
    @Override
    public TextEntry toEntry(final Created source, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new BaseEntry.TextEntry(id, OrderEvents.Created.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final Created source, final int version, final String id, final Metadata metadata) {
      final String serialization = JsonSerialization.serialized(source);
      return new BaseEntry.TextEntry(id, OrderEvents.Created.class, 1, serialization, version, metadata);
    }
}
