// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.vgoairlines.airportterminal.event.FlightDeparted;
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

  @Override
  public void init(final Grid grid) {
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
            new FlightDepartedReceiver(grid),
            new FlightDepartedAdapter(),
            FlightDeparted.class,
            String.class,
            Message.class));

    airTrafficControl.register(Covey.of(
        new MessageSender(airTrafficControl.connection()),
        received -> {},
        new ControllerProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    this.dispatcher = new ExchangeDispatcher(airTrafficControl);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        airTrafficControl.close();

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