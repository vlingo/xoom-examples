// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.infrastructure.exchange;

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

    final ConnectionSettings Settings =
                ExchangeSettings.of("inventory-exchange").mapToConnection();

    final Exchange exchange =
                ExchangeFactory.fanOutInstance(Settings, "inventory-exchange", true);

    final ConnectionSettings inventoryExchangeSettings =
                ExchangeSettings.of("inventory-exchange").mapToConnection();

    final Exchange inventoryExchange =
                ExchangeFactory.fanOutInstance(inventoryExchangeSettings, "inventory-exchange", true);

    inventoryExchange.register(Covey.of(
        new MessageSender(inventoryExchange.connection()),
        received -> {},
        new AircraftProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        exchange.close();
        inventoryExchange.close();

        System.out.println("\n");
        System.out.println("==================");
        System.out.println("Stopping exchange.");
        System.out.println("==================");
    }));

    instance = new ExchangeBootstrap(inventoryExchange);

    return instance;
  }

  private ExchangeBootstrap(final Exchange ...exchanges) {
    this.dispatcher = new ExchangeDispatcher(exchanges);
  }

  public Dispatcher dispatcher() {
    return dispatcher;
  }

}