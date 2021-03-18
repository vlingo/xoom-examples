package io.vlingo.cars;

import io.vlingo.actors.World;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.cars.persistence.StorageProvider;
import io.vlingo.cars.resource.CarResource;

public class Bootstrap {
    private static Bootstrap instance = null;

    private final World world;
    private final Server server;

    private Bootstrap(int port) throws Exception {
        world = World.startWithDefaults("vlingo-cars");

        StorageProvider.using(world, Config.load());
        CarResource carResource = new CarResource(world.stage());
        Resources allResources = Resources.are(carResource.routes());

        server = Server.startWith(world.stage(), allResources, port, Configuration.Sizing.define(), new Configuration.Timing(4L, 2L, 100L));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.server.stop();
                System.out.println("=========================");
                System.out.println("Stopping vlingo-cars");
                System.out.println("=========================");
            }
        }));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=========================");
        System.out.println("Starting: vlingo-cars");
        System.out.println("=========================");

        instance = new Bootstrap(18080);
    }
}
