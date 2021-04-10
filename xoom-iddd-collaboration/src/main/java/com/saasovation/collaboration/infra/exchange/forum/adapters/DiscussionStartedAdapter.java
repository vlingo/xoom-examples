package com.saasovation.collaboration.infra.exchange.forum.adapters;

import com.saasovation.collaboration.model.forum.Events;
import io.vlingo.xoom.lattice.exchange.camel.adapter.JsonCamelExchangeAdapter;
import org.apache.camel.CamelContext;

public class DiscussionStartedAdapter extends JsonCamelExchangeAdapter<Events.DiscussionStarted, Events.DiscussionStarted> {

  public DiscussionStartedAdapter(final CamelContext camelContext) {super(camelContext, Events.DiscussionStarted.class);}

}
