package io.examples.calculation.domain;

import io.examples.infrastructure.CalculationQueryProvider;
import io.examples.infrastructure.MockDispatcher;
import io.vlingo.actors.Stage;
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

import java.util.Arrays;

public class CalculationTests {

    @Test
    public void testAddition() {
        final CalculationState state =
                Calculation.calculate(stage(), Operation.ADDITION, 2, 2).await();

        Assertions.assertEquals(4, state.result());
    }

    @Test
    public void testRepeatedAddition() {
        final CalculationState firstState =
                Calculation.calculate(stage(), Operation.ADDITION, 3, 2).await();

        Assertions.assertEquals(5, firstState.result());

        final CalculationState secondState =
                Calculation.calculate(stage(), Operation.ADDITION, 2, 3).await();

        Assertions.assertEquals(5, secondState.result());
        Assertions.assertEquals(firstState.id().value, secondState.id().value);
    }


    @Test
    public void testSubtraction() {
        final CalculationState state =
                Calculation.calculate(stage(), Operation.SUBTRACTION, 3, 2).await();

        Assertions.assertEquals(1, state.result());
    }

    @Test
    public void testMultiplication() {
        final CalculationState state =
                Calculation.calculate(stage(), Operation.MULTIPLICATION, 5, 5).await();

        Assertions.assertEquals(25, state.result());
    }

    @BeforeEach
    public void setUp() {
        final World world = TestWorld.start("calculation-test").world();

        final MapQueryExpression objectQuery =
                MapQueryExpression.using(Calculation.class, "find", MapQueryExpression.map("id", "id"));

        final ObjectStore objectStore =
                world.stage().actorFor(ObjectStore.class, InMemoryObjectStoreActor.class, Arrays.asList(new MockDispatcher()));

        final StateObjectMapper stateObjectMapper =
                StateObjectMapper.with(CalculationState.class, new Object(), new Object());

        final ObjectTypeRegistry.Info<CalculationState> info =
                new ObjectTypeRegistry.Info(objectStore, CalculationState.class,
                        "ObjectStore", objectQuery, stateObjectMapper);

        new ObjectTypeRegistry(world).register(info);

        CalculationQueryProvider.using(world.stage(), objectStore);
    }

    private Stage stage() {
        return TestWorld.Instance.get().world().stage();
    }

    @AfterEach
    public void tearDown() {
        TestWorld.Instance.get().world().terminate();
        CalculationQueryProvider.reset();
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

