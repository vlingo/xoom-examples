package io.examples.stock.domain;

import io.examples.infrastructure.MockDispatcher;
import io.examples.infrastructure.StockQueryProvider;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.lattice.grid.spaces.Item;
import io.vlingo.lattice.model.object.ObjectTypeRegistry;
import io.vlingo.lattice.model.object.ObjectTypeRegistry.Info;
import io.vlingo.symbio.store.MapQueryExpression;
import io.vlingo.symbio.store.object.ObjectStore;
import io.vlingo.symbio.store.object.StateObjectMapper;
import io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * {@code StockTest} performs tests over {@link StockEntity}
 * to ensure data consistency and state management.
 *
 * @author Danilo Ambrosio
 */
public class StockTest {

    @Test
    public void testAvailableQuantityManipulation() {
        final ItemId itemId = ItemId.of(1l);
        final Location location = Location.LA;
        final Stage stage = TestWorld.Instance.get().stage();

        final StockState state =
                Stock.openIn(stage, Location.LA)
                        .andThenTo(updated -> Stock.increaseAvailabilityFor(stage, location, itemId, 500))
                        .andThenTo(updated -> Stock.increaseAvailabilityFor(stage, location, itemId, 100))
                        .andThenTo(updated -> Stock.unload(stage, location, itemId, 200))
                        .await();

        Assertions.assertEquals(400, state.quantityFor(itemId));
    }

    @BeforeEach
    public void setUp() {
        final World world = TestWorld.start("stock-test").world();

        final MapQueryExpression objectQuery =
                MapQueryExpression.using(Stock.class, "find", MapQueryExpression.map("id", "id"));

        final ObjectStore objectStore =
                world.stage().actorFor(ObjectStore.class, InMemoryObjectStoreActor.class, new MockDispatcher());

        final StateObjectMapper stateObjectMapper =
                StateObjectMapper.with(Stock.class, new Object(), new Object());

        final Info<StockState> info =
                new Info(objectStore, StockState.class,
                        "ObjectStore", objectQuery, stateObjectMapper);

        new ObjectTypeRegistry(world).register(info);

        StockQueryProvider.using(world.stage(), objectStore);
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
