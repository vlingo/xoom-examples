package com.saasovation.agilepm.infra.exchange.product.adapters;

import com.saasovation.agilepm.model.product.Events;
import io.vlingo.xoom.lattice.exchange.camel.adapter.JsonCamelExchangeAdapter;
import org.apache.camel.CamelContext;

public class ProductDiscussionRequestedEventAdapter extends JsonCamelExchangeAdapter<Events.ProductDiscussionRequested, Events.ProductDiscussionRequested> {

    public ProductDiscussionRequestedEventAdapter(CamelContext camelContext) {
        super(camelContext, Events.ProductDiscussionRequested.class);
    }
}
