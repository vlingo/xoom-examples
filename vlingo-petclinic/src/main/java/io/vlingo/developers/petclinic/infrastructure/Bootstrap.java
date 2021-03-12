package io.vlingo.developers.petclinic.infrastructure;

import io.vlingo.developers.petclinic.infrastructure.resource.ClientResource;
import io.vlingo.developers.petclinic.infrastructure.persistence.ProjectionDispatcherProvider;
import io.vlingo.developers.petclinic.infrastructure.persistence.CommandModelJournalProvider;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.developers.petclinic.infrastructure.resource.SpecialtyTypeResource;
import io.vlingo.developers.petclinic.infrastructure.resource.VeterinarianResource;
import io.vlingo.developers.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.developers.petclinic.infrastructure.resource.AnimalTypeResource;
import io.vlingo.developers.petclinic.infrastructure.resource.PetResource;

import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

public class Bootstrap {
  private static Bootstrap instance;
  private static int DefaultPort = 18080;

  private final Server server;
  private final World world;

  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults("vlingo-petclinic");

    final Stage stage =
            world.stageNamed("vlingo-petclinic", Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelJournalProvider.using(stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage).storeDispatcher);

    final PetResource petResource = new PetResource(stage);
    final AnimalTypeResource animalTypeResource = new AnimalTypeResource(stage);
    final SpecialtyTypeResource specialtyTypeResource = new SpecialtyTypeResource(stage);
    final ClientResource clientResource = new ClientResource(stage);
    final VeterinarianResource veterinarianResource = new VeterinarianResource(stage);

    Resources allResources = Resources.are(
        petResource.routes(),
        animalTypeResource.routes(),
        specialtyTypeResource.routes(),
        clientResource.routes(),
        veterinarianResource.routes()
    );

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (instance != null) {
        instance.server.stop();

        System.out.println("\n");
        System.out.println("=========================");
        System.out.println("Stopping vlingo-petclinic.");
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
    System.out.println("service: vlingo-petclinic.");
    System.out.println("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DefaultPort;
      System.out.println("vlingo-petclinic: Command line does not provide a valid port; defaulting to: " + port);
    }

    instance = new Bootstrap(port);
  }
}
