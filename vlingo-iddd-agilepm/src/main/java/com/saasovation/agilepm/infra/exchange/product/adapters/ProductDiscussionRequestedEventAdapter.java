package com.saasovation.agilepm.infra.exchange.product.adapters;

import com.saasovation.agilepm.model.product.Events;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.ExchangeBuilder;

public class ProductDiscussionRequestedEventAdapter implements ExchangeAdapter<Events.ProductDiscussionRequested, Events.ProductDiscussionRequested, Exchange> {

    private final CamelContext camelContext;

    public ProductDiscussionRequestedEventAdapter(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public Events.ProductDiscussionRequested fromExchange(Exchange exchangeMessage) {
        return null;
    }

    @Override
    public Exchange toExchange(Events.ProductDiscussionRequested localMessage) {
        return ExchangeBuilder.anExchange(camelContext)
                .withHeader("event", Events.ProductDiscussionRequested.class.getSimpleName())
                .withBody(JsonSerialization.serialized(localMessage))
                .build();
    }

    @Override
    public boolean supports(Object exchangeMessage) {
        if (exchangeMessage instanceof Exchange) {
            final Message message = ((Exchange) exchangeMessage).getMessage();
            return Events.ProductDiscussionRequested.class.getSimpleName().equalsIgnoreCase(message.getHeader("event", String.class));
        }
        return false;
    }
}
