// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.lattice.exchange.ConnectionSettings;
import io.vlingo.xoom.lattice.exchange.Covey;
import io.vlingo.xoom.lattice.exchange.Exchange;
import io.vlingo.xoom.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;
import io.vlingo.xoom.lattice.exchange.rabbitmq.MessageSender;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
import io.vlingo.xoom.turbo.exchange.ExchangeSettings;

public class ExchangeBootstrap implements ExchangeInitializer {

  private Dispatcher dispatcher;

  public void init(final Grid stage) {
    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings aircraftMonitoringExchangeSettings =
                ExchangeSettings.of("aircraft-monitoring-exchange").mapToConnection();

    final Exchange aircraftMonitoringExchange =
                ExchangeFactory.fanOutInstance(aircraftMonitoringExchangeSettings, "aircraft-monitoring-exchange", true);

    this.dispatcher = new ExchangeDispatcher(aircraftMonitoringExchange);

    aircraftMonitoringExchange.register(Covey.of(
        new MessageSender(aircraftMonitoringExchange.connection()),
        new AirTrafficStatusReceiver(stage),
        new AirTrafficStatusAdapter(),
        AirTrafficStatusAdapter.supportedLocalClass(),
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
  }

  public Dispatcher dispatcher() {
    return dispatcher;
  }

}