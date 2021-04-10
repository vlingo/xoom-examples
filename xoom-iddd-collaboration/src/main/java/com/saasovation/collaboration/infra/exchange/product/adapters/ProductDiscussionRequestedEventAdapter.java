package com.saasovation.collaboration.infra.exchange.product.adapters;

import com.saasovation.collaboration.infra.exchange.product.model.ProductDiscussionRequested;
import io.vlingo.xoom.lattice.exchange.camel.adapter.JsonCamelExchangeAdapter;
import org.apache.camel.CamelContext;

public class ProductDiscussionRequestedEventAdapter extends JsonCamelExchangeAdapter<ProductDiscussionRequested, ProductDiscussionRequested> {

  public ProductDiscussionRequestedEventAdapter(CamelContext camelContext) {
    super(camelContext, ProductDiscussionRequested.class);
  }
  
}
