package io.examples.order.domain;

import io.examples.infrastructure.ApplicationRegistry;
import io.examples.infrastructure.MockDispatcher;
import io.examples.infrastructure.OrderQueryProvider;
import io.examples.infrastructure.messaging.MessagingClient;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.lattice.model.object.ObjectTypeRegistry;
import io.vlingo.symbio.store.MapQueryExpression;
import io.vlingo.symbio.store.object.ObjectStore;
import io.vlingo.symbio.store.object.StateObjectMapper;
import io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

/**
 * {@code OrderTest} performs tests over {@link OrderEntity}
 * to ensure data consistency and state management.
 *
 * @author Danilo Ambrosio
 */
public class OrderTest {

    private MessagingClient messagingClient;
    private ApplicationRegistry applicationRegistry;

    @Test
    public void testOrderInformationRead() {
        final OrderState order =
                Order.register(applicationRegistry, ProductId.of(1l), 40, Site.LA).await();

        Assertions.assertEquals(40, order.quantity());
        Assertions.assertEquals(ProductId.of(1l), order.productId());
        Mockito.verify(messagingClient, times(1)).publish(any(OrderWasRegistered.class));
    }

    @BeforeEach
    public void setUp() {
        final World world = TestWorld.start("order-test").world();

        messagingClient = Mockito.mock(MessagingClient.class);
        applicationRegistry = Mockito.mock(ApplicationRegistry.class);
        Mockito.when(applicationRegistry.retrieveStage()).thenReturn(world.stage());
        Mockito.when(applicationRegistry.retrieveMessagingClient()).thenReturn(messagingClient);

        final MapQueryExpression objectQuery =
                MapQueryExpression.using(Order.class, "find", MapQueryExpression.map("id", "id"));

        final ObjectStore objectStore =
                world.stage().actorFor(ObjectStore.class, InMemoryObjectStoreActor.class, new MockDispatcher());

        final StateObjectMapper stateObjectMapper =
                StateObjectMapper.with(Order.class, new Object(), new Object());

        final ObjectTypeRegistry.Info<OrderState> info =
                new ObjectTypeRegistry.Info(objectStore, OrderState.class,
                        "ObjectStore", objectQuery, stateObjectMapper);

        new ObjectTypeRegistry(world).register(info);

        OrderQueryProvider.using(world.stage(), objectStore);
    }

    @AfterEach
    public void tearDown() {
        TestWorld.Instance.get().world().terminate();
        pause();
    }

    private void pause() {
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            // ignore
        }
    }
}
