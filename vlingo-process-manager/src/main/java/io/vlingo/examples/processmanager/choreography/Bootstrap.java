import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.examples.processmanager.choreography.ShoppingCart;
import io.vlingo.examples.processmanager.choreography.ShoppingCartEntity;
import io.vlingo.examples.processmanager.choreography.ShoppingCartResource;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

public class Bootstrap {
    private static Bootstrap instance;
    public final World world;
    public final Server server;

    private Bootstrap() {
        this.world = World.startWithDefaults("frontservice");
        final ShoppingCartResource shoppingCartResource = new ShoppingCartResource(world);

        final Resources resources = Resources.are(shoppingCartResource.routes());

        this.server = Server.startWith(world.stage(), resources, 8081, Configuration.Sizing.define(), Configuration.Timing.define());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (instance != null) {
                    instance.server.stop();

                    System.out.println("\n");
                    System.out.println("=======================");
                    System.out.println("Stopping frontservice.");
                    System.out.println("=======================");
                    pause();
                }
            }
        });
    }

    public static final Bootstrap instance() {
        if (instance == null) instance = new Bootstrap();
        return instance;
    }

    public static final void terminateTestInstance() {
        instance = null;
    }

    public static void main(final String[] args) throws Exception {
        System.out.println("=======================");
        System.out.println("service: started.");
        System.out.println("=======================");
        Bootstrap.instance();
    }

    private void pause() {
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            // ignore
        }
    }
}
