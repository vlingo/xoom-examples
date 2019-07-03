package io.vlingo.examples.ecommerce.infra.order;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.OrderEvents;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class PaymentReceivedEventAdapter implements EntryAdapter<OrderEvents.PaymentReceived,BaseEntry.TextEntry> {

    @Override
    public OrderEvents.PaymentReceived fromEntry(final BaseEntry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), OrderEvents.PaymentReceived.class);
    }

    @Override
    public BaseEntry.TextEntry toEntry(final OrderEvents.PaymentReceived source, final Metadata metadata) {
        return toEntry(source, source.orderId, metadata);
    }

    @Override
    public BaseEntry.TextEntry toEntry(final OrderEvents.PaymentReceived source, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new BaseEntry.TextEntry(id, OrderEvents.PaymentReceived.class, 1, serialization, metadata);
    }
}
