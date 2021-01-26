package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import io.vlingo.actors.Stage;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.xoom.exchange.ExchangeSettings;
import io.vlingo.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.lattice.exchange.ConnectionSettings;
import io.vlingo.lattice.exchange.rabbitmq.Message;
import io.vlingo.lattice.exchange.rabbitmq.MessageSender;
import io.vlingo.lattice.exchange.Covey;
import io.vlingo.symbio.store.dispatch.Dispatcher;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;

public class ExchangeBootstrap {

  private static ExchangeBootstrap instance;

  private final Dispatcher dispatcher;

  public static ExchangeBootstrap init(final Stage stage) {
    if(instance != null) {
      return instance;
    }

    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings airTrafficControlSettings =
                ExchangeSettings.of("air-traffic-control").mapToConnection();

    final Exchange airTrafficControl =
                ExchangeFactory.fanOutInstance(airTrafficControlSettings, "air-traffic-control", true);

    airTrafficControl.register(Covey.of(
        new MessageSender(airTrafficControl.connection()),
        received -> {},
        new FlightProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    airTrafficControl.register(Covey.of(
        new MessageSender(airTrafficControl.connection()),
        received -> {},
        new ControllerProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        airTrafficControl.close();

        System.out.println("\n");
        System.out.println("==================");
        System.out.println("Stopping exchange.");
        System.out.println("==================");
    }));

    instance = new ExchangeBootstrap(airTrafficControl);

    return instance;
  }

  private ExchangeBootstrap(final Exchange ...exchanges) {
    this.dispatcher = new ExchangeDispatcher(exchanges);
  }

  public Dispatcher dispatcher() {
    return dispatcher;
  }

}