package com.saasovation.agilepm.infra.exchange.forum.adapters;

import com.saasovation.agilepm.infra.exchange.forum.model.DiscussionStarted;
import io.vlingo.lattice.exchange.camel.adapter.JsonCamelExchangeAdapter;
import org.apache.camel.CamelContext;

public class DiscussionStartedAdapter extends JsonCamelExchangeAdapter<DiscussionStarted, DiscussionStarted> {

    public DiscussionStartedAdapter(CamelContext camelContext) {
        super(camelContext, DiscussionStarted.class);
    }
}
