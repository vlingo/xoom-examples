package io.vlingo.xoom.examples.petclinic.infrastructure;

import io.vlingo.xoom.examples.petclinic.infrastructure.resource.ClientResource;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.ProjectionDispatcherProvider;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.CommandModelJournalProvider;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.examples.petclinic.infrastructure.resource.SpecialtyTypeResource;
import io.vlingo.xoom.examples.petclinic.infrastructure.resource.VeterinarianResource;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.examples.petclinic.infrastructure.resource.AnimalTypeResource;
import io.vlingo.xoom.examples.petclinic.infrastructure.resource.PetResource;

import io.vlingo.xoom.actors.GridAddressFactory;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.common.identity.IdentityGeneratorType;
import io.vlingo.xoom.http.resource.Configuration.Sizing;
import io.vlingo.xoom.http.resource.Configuration.Timing;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.Server;

public class Bootstrap {
  private static Bootstrap instance;
  private static int DefaultPort = 18080;

  private final Server server;
  private final World world;

  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults("xoom-petclinic");

    final Stage stage =
            world.stageNamed("xoom-petclinic", Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

    final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(world);
    final StatefulTypeRegistry statefulTypeRegistry = new StatefulTypeRegistry(world);
    final QueryModelStateStoreProvider queryModelStateStoreProvider = QueryModelStateStoreProvider.using(stage, statefulTypeRegistry);
    CommandModelJournalProvider.using(stage, sourcedTypeRegistry, ProjectionDispatcherProvider.using(stage, queryModelStateStoreProvider).storeDispatcher);

    final PetResource petResource = new PetResource(stage, queryModelStateStoreProvider.petQueries);
    final AnimalTypeResource animalTypeResource = new AnimalTypeResource(stage, queryModelStateStoreProvider.animalTypeQueries);
    final SpecialtyTypeResource specialtyTypeResource = new SpecialtyTypeResource(stage, queryModelStateStoreProvider.specialtyTypeQueries);
    final ClientResource clientResource = new ClientResource(stage, queryModelStateStoreProvider.clientQueries);
    final VeterinarianResource veterinarianResource = new VeterinarianResource(stage, queryModelStateStoreProvider.veterinarianQueries);

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
        System.out.println("Stopping xoom-petclinic.");
        System.out.println("=========================");
      }
    }));
  }

  public static void stopServer() throws Exception {
    if (instance == null) {
      throw new IllegalStateException("PetClinic server is not running");
    }
    instance.server.stop();
  }

  public static void main(final String[] args) throws Exception {
    System.out.println("=========================");
    System.out.println("service: xoom-petclinic.");
    System.out.println("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DefaultPort;
      System.out.println("xoom-petclinic: Command line does not provide a valid port; defaulting to: " + port);
    }

    instance = new Bootstrap(port);
  }
}
