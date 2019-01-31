package io.vlingo.examples.ecommerce;

import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.examples.ecommerce.infra.MockJournalListener;
import io.vlingo.examples.ecommerce.infra.cart.CartAllItemsRemoveEventAdapter;
import io.vlingo.examples.ecommerce.infra.cart.CartCreatedEventAdapter;
import io.vlingo.examples.ecommerce.infra.cart.CartProductQuantityChangedEventAdapter;
import io.vlingo.examples.ecommerce.infra.order.OrderCreatedEventAdapter;
import io.vlingo.examples.ecommerce.model.*;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;

import static io.vlingo.examples.ecommerce.model.CartEvents.*;
import static io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;

public class Bootstrap {
    private static Bootstrap instance;
    public final Server server;
    public final World world;

    @SuppressWarnings({"unchecked"})
    private Bootstrap() {
        world = World.startWithDefaults("cartservice");

        MockJournalListener listener = new MockJournalListener();
        Journal<String> journal = Journal.using(world.stage(), InMemoryJournalActor.class, listener);

        SourcedTypeRegistry registry = new SourcedTypeRegistry(world);
        registry.register(new Info(journal, CartEntity.class, CartEntity.class.getSimpleName()));
        registry.register(new Info(journal, OrderEntity.class, OrderEntity.class.getSimpleName()));

        registry.info(OrderEntity.class)
                .registerEntryAdapter(OrderEvents.Created.class, new OrderCreatedEventAdapter(),
                        journal::registerEntryAdapter);

        registry.info(CartEntity.class)
                .registerEntryAdapter(CreatedForUser.class, new CartCreatedEventAdapter(),
                        journal::registerEntryAdapter)
                .registerEntryAdapter(ProductQuantityChangeEvent.class, new CartProductQuantityChangedEventAdapter(),
                        journal::registerEntryAdapter)
                .registerEntryAdapter(AllItemsRemovedEvent.class, new CartAllItemsRemoveEventAdapter(),
                        journal::registerEntryAdapter);


        final CartResource cartResource = new CartResource(world);
        final OrderResource orderResource = new OrderResource(world);

        final Resources resources = Resources.are(
                cartResource.routes(),
                orderResource.routes());
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

    /*static <A extends Actor, T> Journal<T> using(
            final Stage stage,
            final Class<A> implementor,
            final JournalListener<T> listener
    ) {
        return (Journal<T>) stage.actorFor(Journal.class, implementor, listener);
    }
*/
    static Bootstrap instance() {
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

    Completes<Boolean> serverStartup() {
        return server.startUp();
    }

    public void stop() throws InterruptedException {
        //todo: this call fails after timeout / does not throw exception
        //instance.server.shutDown().await(10000);
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
