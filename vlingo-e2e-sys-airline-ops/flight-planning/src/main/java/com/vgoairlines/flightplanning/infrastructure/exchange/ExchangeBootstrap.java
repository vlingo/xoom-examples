package com.vgoairlines.flightplanning.infrastructure.exchange;

import com.vgoairlines.inventory.event.AircraftConsigned;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.exchange.ConnectionSettings;
import io.vlingo.lattice.exchange.Covey;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.lattice.exchange.rabbitmq.Message;
import io.vlingo.lattice.exchange.rabbitmq.MessageSender;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.exchange.ExchangeSettings;

public class ExchangeBootstrap {

  private static ExchangeBootstrap instance;

  private final Dispatcher dispatcher;

  public static ExchangeBootstrap init(final Stage stage) {
    if(instance != null) {
      return instance;
    }

    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings flightPlanningExchangeSettings =
                ExchangeSettings.of("flight-planning-exchange").mapToConnection();

    final Exchange flightPlanningExchange =
                ExchangeFactory.fanOutInstance(flightPlanningExchangeSettings, "flight-planning-exchange", true);

    flightPlanningExchange.register(Covey.of(
        new MessageSender(flightPlanningExchange.connection()),
        received -> {},
        new FlightProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    flightPlanningExchange.register(Covey.of(
      new MessageSender(flightPlanningExchange.connection()),
        new AircraftConsignedReceiver(stage),
        new AircraftConsignedAdapter("VgoAirlines:Inventory:com.vgoairlines.inventory:AircraftConsigned:4.0.1"),
        AircraftConsigned.class,
        String.class,
        Message.class));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        flightPlanningExchange.close();

        System.out.println("\n");
        System.out.println("==================");
        System.out.println("Stopping exchange.");
        System.out.println("==================");
    }));

    instance = new ExchangeBootstrap(flightPlanningExchange);

    return instance;
  }

  private ExchangeBootstrap(final Exchange ...exchanges) {
    this.dispatcher = new ExchangeDispatcher(exchanges);
  }

  public Dispatcher dispatcher() {
    return dispatcher;
  }

}