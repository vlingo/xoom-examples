package com.saasovation.collaboration.infra.exchange.product.adapters;

import com.saasovation.collaboration.infra.exchange.product.model.ProductDiscussionRequested;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

public class ProductDiscussionRequestedEventAdapter implements ExchangeAdapter<ProductDiscussionRequested, ProductDiscussionRequested, Exchange> {

  public ProductDiscussionRequestedEventAdapter(CamelContext camelContext) {
  }

  @Override
  public ProductDiscussionRequested fromExchange(Exchange exchangeMessage) {
    final String body = exchangeMessage.getMessage().getBody(String.class);
    return JsonSerialization.deserialized(body, ProductDiscussionRequested.class);
  }

  @Override
  public Exchange toExchange(ProductDiscussionRequested localMessage) {
    return null;
  }

  @Override
  public boolean supports(Object exchangeMessage) {
    if (exchangeMessage instanceof Exchange) {
      final Message message = ((Exchange) exchangeMessage).getMessage();
      return ProductDiscussionRequested.class.getSimpleName().equalsIgnoreCase(message.getHeader("event", String.class));
    }
    return false;
  }
}
