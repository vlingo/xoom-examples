package io.vlingo.examples.ecommerce;

import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.examples.ecommerce.infra.MockJournalListener;
import io.vlingo.examples.ecommerce.infra.cart.CartAllItemsRemoveEventAdapter;
import io.vlingo.examples.ecommerce.infra.cart.CartCreatedEventAdapter;
import io.vlingo.examples.ecommerce.infra.cart.CartProductQuantityChangedEventAdapter;
import io.vlingo.examples.ecommerce.infra.order.OrderCreatedEventAdapter;
import io.vlingo.examples.ecommerce.infra.order.PaymentReceivedEventAdapter;
import io.vlingo.examples.ecommerce.infra.order.ShippedEventAdapter;
import io.vlingo.examples.ecommerce.model.*;
import io.vlingo.examples.ecommerce.model.CartEvents.AllItemsRemovedEvent;
import io.vlingo.examples.ecommerce.model.CartEvents.CreatedForUser;
import io.vlingo.examples.ecommerce.model.CartEvents.ProductQuantityChangeEvent;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;

public class Bootstrap {
    private static Bootstrap instance;
    private final Server server;
    private final World world;

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Bootstrap(final int portNumber) {
        world = World.startWithDefaults("cartservice");

        MockJournalListener listener = new MockJournalListener();
        Journal<String> journal = Journal.using(world.stage(), InMemoryJournalActor.class, listener);

        SourcedTypeRegistry registry = new SourcedTypeRegistry(world);
        registry.register(new Info(journal, CartActor.class, CartActor.class.getSimpleName()));
        registry.register(new Info(journal, OrderActor.class, OrderActor.class.getSimpleName()));

        registry.info(OrderActor.class)
                .registerEntryAdapter(OrderEvents.Created.class, new OrderCreatedEventAdapter())
                .registerEntryAdapter(OrderEvents.PaymentReceived.class, new PaymentReceivedEventAdapter())
                .registerEntryAdapter(OrderEvents.OrderShipped.class, new ShippedEventAdapter());

        registry.info(CartActor.class)
                .registerEntryAdapter(CreatedForUser.class, new CartCreatedEventAdapter())
                .registerEntryAdapter(ProductQuantityChangeEvent.class, new CartProductQuantityChangedEventAdapter())
                .registerEntryAdapter(AllItemsRemovedEvent.class, new CartAllItemsRemoveEventAdapter());


        final CartResource cartResource = new CartResource(world);
        final OrderResource orderResource = new OrderResource(world);

        final Resources resources = Resources.are(
                cartResource.routes(),
                orderResource.routes());
        this.server = Server.startWith(world.stage(),
                resources,
                portNumber,
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

    static Bootstrap instance(final int portNumber) {
        if (instance == null) {
            instance = new Bootstrap(portNumber);
        }
        return instance;
    }
    static Bootstrap instance() {
      return instance(8081);
    }

    public static void main(final String[] args) {
        System.out.println("=======================");
        System.out.println("service: ecommerce-started.");
        System.out.println("=======================");
        Bootstrap.instance();
    }

    Completes<Boolean> serverStartup() {
        return server.startUp();
    }

    void stop() {
        Bootstrap.instance().server.stop();
        instance = null;
    }

    private void pause() {
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            // ignore
        }
    }
}
