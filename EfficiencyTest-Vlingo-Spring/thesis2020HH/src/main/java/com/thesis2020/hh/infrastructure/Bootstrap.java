package com.thesis2020.hh.infrastructure;


import com.thesis2020.hh.infrastructure.persistence.CommandModelStateStoreProvider;
import com.thesis2020.hh.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import com.thesis2020.hh.resource.GreetingResource;
import com.thesis2020.hh.resource.HelloResources;
import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

public class Bootstrap {
  private static Bootstrap instance;
  private static int DefaultPort = 18080;

  private final Server server;
  private final World world;

  public Bootstrap(final int port) throws Exception {

    world = World.startWithDefaults("thesis2020HH");
//    world = World.start("thesis2020HH");
    
    final Stage stage =
            world.stageNamed("thesis2020HH", Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

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
        System.out.println("Stopping thesis2020HH.");
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
    System.out.println("service: thesis2020HH.");
    System.out.println("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DefaultPort;
      System.out.println("thesis2020HH: Command line does not provide a valid port; defaulting to: " + port);
    }

    instance = new Bootstrap(port);
  }
}
