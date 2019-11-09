package com.saasovation.agilepm.infra.exchange.forum.adapters;

import com.saasovation.agilepm.infra.exchange.forum.model.DiscussionStarted;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

public class DiscussionStartedAdapter implements ExchangeAdapter<DiscussionStarted, DiscussionStarted, Exchange> {

    @Override
    public DiscussionStarted fromExchange(Exchange exchangeMessage) {
        final String body = exchangeMessage.getMessage().getBody(String.class);
        return JsonSerialization.deserialized(body, DiscussionStarted.class);
    }

    @Override
    public Exchange toExchange(DiscussionStarted localMessage) {
        return null;
    }

    @Override
    public boolean supports(Object exchangeMessage) {
        if (exchangeMessage instanceof Exchange) {
            final Message message = ((Exchange) exchangeMessage).getMessage();
            return DiscussionStarted.class.getSimpleName().equalsIgnoreCase(message.getHeader("event", String.class));
        }
        return false;
    }
}
