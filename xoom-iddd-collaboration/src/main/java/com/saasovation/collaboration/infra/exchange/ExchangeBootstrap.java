package com.saasovation.collaboration.infra.exchange;

import com.saasovation.collaboration.infra.exchange.forum.adapters.DiscussionStartedAdapter;
import com.saasovation.collaboration.infra.exchange.product.adapters.ProductDiscussionRequestedEventAdapter;
import com.saasovation.collaboration.infra.exchange.product.model.ProductDiscussionRequested;
import com.saasovation.collaboration.infra.exchange.product.receivers.ProductDiscussionRequestedEventReceiver;
import com.saasovation.collaboration.model.forum.Events.DiscussionStarted;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.lattice.exchange.ExchangeSender;
import io.vlingo.xoom.lattice.exchange.camel.CamelExchange;
import io.vlingo.xoom.lattice.exchange.camel.CoveyFactory;
import io.vlingo.xoom.lattice.exchange.camel.sender.ExchangeSenders;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.engine.DefaultConsumerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.apache.camel.support.DefaultRegistry;

public class ExchangeBootstrap {

  private final Stage stage;

  public ExchangeBootstrap(final World world) {
    stage = world.stage();
  }

  @SuppressWarnings("resource")
  public io.vlingo.xoom.lattice.exchange.Exchange initExchange() throws Exception {
    DefaultCamelContext camelContext = new DefaultCamelContext(new DefaultRegistry());
    camelContext.start();

    DefaultProducerTemplate producerTemplate = new DefaultProducerTemplate(camelContext);
    DefaultConsumerTemplate consumerTemplate = new DefaultConsumerTemplate(camelContext);

    producerTemplate.start();
    consumerTemplate.start();
    
    final String exchangeUri = "rabbitmq:agile-iddd-product?hostname=localhost&portNumber=5672";

    final CamelExchange camelExchange = new CamelExchange(camelContext, "agilepm-exchange", exchangeUri);
    final ExchangeSender<Exchange> sender = ExchangeSenders.sendingTo(exchangeUri, camelContext);

    camelExchange.register(CoveyFactory.build(sender, new NoOpReceiver<>(),
                                        new DiscussionStartedAdapter(camelContext), DiscussionStarted.class, DiscussionStarted.class))
                 .register(CoveyFactory.build(sender, new ProductDiscussionRequestedEventReceiver(stage),
                                    new ProductDiscussionRequestedEventAdapter(camelContext), ProductDiscussionRequested.class, ProductDiscussionRequested.class));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      producerTemplate.stop();
      consumerTemplate.stop();
      camelExchange.close();

      System.out.println("\n");
      System.out.println("=======================");
      System.out.println("Stopping camel exchange.");
      System.out.println("=======================");
    }));

    return camelExchange;
  }

}
