package io.vlingo.xoom.examples.cars;

import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.http.resource.Configuration;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.Server;
import io.vlingo.xoom.examples.cars.persistence.StorageProvider;
import io.vlingo.xoom.examples.cars.resource.CarResource;

public class Bootstrap {
    // TODO Switch to Xoom!

    private static final int port = 18080;

    private static String parseNameFromArguments(String[] args) {
        if (args.length == 0) {
            System.err.println("The node must be named with a command-line argument.");
            System.exit(1);
        }
        else if (args.length > 1) {
            System.err.println("Too many arguments; provide node name only.");
            System.exit(1);
        }
        return args[0];
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=========================");
        System.out.println("Starting: xoom-distributed-cqrs");
        System.out.println("=========================");

        String nodeName = parseNameFromArguments(args);

        final Server server;
        Grid grid = Grid.start("xoom-distributed-cqrs", nodeName);

        StorageProvider.using(grid.world(), CarConfig.load());

        if ("node1".equals(nodeName)) {
            CarResource carResource = new CarResource(grid);
            Resources allResources = Resources.are(carResource.routes());

            server = Server.startWith(grid.world().stage(), allResources, port, Configuration.Sizing.define(), new Configuration.Timing(4L, 2L, 100L));
        } else {
            server = null;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server != null) {
                server.stop();
            }

            System.out.println("=========================");
            System.out.println("Stopping xoom-distributed-cqrs");
            System.out.println("=========================");
        }));
    }
}
