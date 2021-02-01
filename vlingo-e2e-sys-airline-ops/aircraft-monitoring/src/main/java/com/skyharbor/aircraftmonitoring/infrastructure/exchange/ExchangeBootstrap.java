package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;
import com.skyharbor.airtrafficcontrol.event.FlightDepartedGate;
import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import com.skyharbor.airtrafficcontrol.event.FlightTookOff;
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

    final ConnectionSettings aircraftMonitoringExchangeSettings =
                ExchangeSettings.of("aircraft-monitoring-exchange").mapToConnection();

    final Exchange aircraftMonitoringExchange =
                ExchangeFactory.fanOutInstance(aircraftMonitoringExchangeSettings, "aircraft-monitoring-exchange", true);


    aircraftMonitoringExchange.register(Covey.of(
            new MessageSender(aircraftMonitoringExchange.connection()),
            new FlightDepartedReceiver(stage),
            new FlightDepartedAdapter(),
            FlightDepartedGate.class,
            String.class,
            Message.class));

    aircraftMonitoringExchange.register(Covey.of(
        new MessageSender(aircraftMonitoringExchange.connection()),
        new FlightLandedReceiver(stage),
        new FlightLandedAdapter(),
        FlightLanded.class,
        String.class,
        Message.class));

    aircraftMonitoringExchange.register(Covey.of(
        new MessageSender(aircraftMonitoringExchange.connection()),
        new FlightTookOffReceiver(stage),
        new FlightTookOffAdapter(),
        FlightTookOff.class,
        String.class,
        Message.class));

    aircraftMonitoringExchange.register(Covey.of(
        new MessageSender(aircraftMonitoringExchange.connection()),
        received -> {},
        new FlightProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        aircraftMonitoringExchange.close();

        System.out.println("\n");
        System.out.println("==================");
        System.out.println("Stopping exchange.");
        System.out.println("==================");
    }));

    instance = new ExchangeBootstrap(aircraftMonitoringExchange);

    return instance;
  }

  private ExchangeBootstrap(final Exchange ...exchanges) {
    this.dispatcher = new ExchangeDispatcher(exchanges);
  }

  public Dispatcher dispatcher() {
    return dispatcher;
  }

}