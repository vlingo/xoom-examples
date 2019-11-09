package com.saasovation.collaboration.infra.exchange.forum.adapters;

import com.saasovation.collaboration.model.forum.Events;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.ExchangeBuilder;

public class DiscussionStartedAdapter implements ExchangeAdapter<Events.DiscussionStarted, Events.DiscussionStarted, Exchange> {
  private final CamelContext camelContext;

  public DiscussionStartedAdapter(final CamelContext camelContext) {this.camelContext = camelContext;}

  @Override
  public Events.DiscussionStarted fromExchange(Exchange exchangeMessage) {
    return null;
  }

  @Override
  public Exchange toExchange(Events.DiscussionStarted localMessage) {
    return ExchangeBuilder.anExchange(camelContext)
                          .withHeader("event", Events.DiscussionStarted.class.getSimpleName())
                          .withBody(JsonSerialization.serialized(localMessage)).build();
  }

  @Override
  public boolean supports(Object exchangeMessage) {
    if (exchangeMessage instanceof Exchange) {
      final Message message = ((Exchange) exchangeMessage).getMessage();
      return Events.DiscussionStarted.class.getSimpleName().equalsIgnoreCase(message.getHeader("event", String.class));
    }
    return false;
  }
}
