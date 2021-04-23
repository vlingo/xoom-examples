// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.infrastructure;

import io.vlingo.xoom.lattice.grid.GridAddressFactory;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.common.identity.IdentityGeneratorType;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.Server;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence.CommandModelStateStoreProvider;
import io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.examples.perf.vlingo.resource.GreetingResource;
import io.vlingo.xoom.examples.perf.vlingo.resource.HelloResources;

public class Bootstrap {
  private static Bootstrap instance;
  private static int DefaultPort = 18080;

  private final Server server;
  private final World world;

  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults("xoom-example-perf");
    final Stage stage =
            world.stageNamed("xoom-example-perf", Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelStateStoreProvider.using(stage, statefulTypeRegistry);

    final GreetingResource greetingResource = new GreetingResource(stage);
    final HelloResources helloResources = new HelloResources(stage);

    Resources allResources = Resources.are(
        greetingResource.routes(),
        helloResources.routes()
    );

//    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.defineWith(7, 5, 100));
    
    server = Server.startWithAgent(stage, allResources, port, 4);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (instance != null) {
        instance.server.stop();

        System.out.println("\n");
        System.out.println("=========================");
        System.out.println("Stopping xoom-example-perf");
        System.out.println("=========================");
      }
    }));
  }

  void stopServer() throws Exception {
    if (instance == null) {
      throw new IllegalStateException("Schemata server not running");
    }
    instance.server.stop();
  }

  public static void main(final String[] args) throws Exception {
    System.out.println("=========================");
    System.out.println("service: xoom-example-perf");
    System.out.println("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DefaultPort;
      System.out.println("xoom-example-perf: Command line does not provide a valid port; defaulting to: " + port);
    }

    instance = new Bootstrap(port);
  }
}
