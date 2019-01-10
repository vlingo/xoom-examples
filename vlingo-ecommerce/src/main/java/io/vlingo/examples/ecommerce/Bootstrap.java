package io.vlingo.examples.ecommerce;

import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.examples.ecommerce.infra.AllItemsRemoveEventAdapter;
import io.vlingo.examples.ecommerce.infra.CreatedEventAdapter;
import io.vlingo.examples.ecommerce.infra.MockJournalListener;
import io.vlingo.examples.ecommerce.infra.ProductQuantityChangedEventAdapter;
import io.vlingo.examples.ecommerce.model.CartEntity;
import io.vlingo.examples.ecommerce.model.CartResource;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;

import static io.vlingo.examples.ecommerce.model.CartEvents.*;

public class Bootstrap {
    private static Bootstrap instance;
    private final Server server;

    @SuppressWarnings({ "unchecked" })
    private Bootstrap() {
        World world = World.startWithDefaults("cartservice");

        MockJournalListener listener = new MockJournalListener();
        Journal<String> journal = world.actorFor(Journal.class, InMemoryJournalActor.class, listener);
        journal.registerAdapter(CreatedEvent.class, new CreatedEventAdapter());
        journal.registerAdapter(ProductQuantityChangeEvent.class, new ProductQuantityChangedEventAdapter());
        journal.registerAdapter(AllItemsRemovedEvent.class, new AllItemsRemoveEventAdapter());

        SourcedTypeRegistry registry = new SourcedTypeRegistry(world);
        registry.register(new SourcedTypeRegistry.Info(journal, CartEntity.class, CartEntity.class.getSimpleName()));

        final CartResource cartResource = new CartResource(world);

        final Resources resources = Resources.are(cartResource.routes());
        this.server = Server.startWith(world.stage(),
                resources,
                8081,
                Configuration.Sizing.define(),
                Configuration.Timing.define());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.server.stop();

                System.out.println("\n");
                System.out.println("=======================");
                System.out.println("Stopping ecommerce-service.");
                System.out.println("=======================");
                pause();
            }
        }));
    }

    public Completes<Boolean> serverStartup() {
        return server.startUp();
    }

    public static final Bootstrap instance() {
        if (instance == null) {
            instance = new Bootstrap();
        }
        return instance;
    }

    public static void main(final String[] args) throws Exception {
        System.out.println("=======================");
        System.out.println("service: ecommerce-started.");
        System.out.println("=======================");
        Bootstrap.instance();
    }

    public void stop() throws InterruptedException {
        instance.server.shutDown().await(1000);
    }

    private void pause() {
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            // ignore
        }
    }
}
