package io.examples.calculation.domain;

import io.vlingo.actors.Stage;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class CalculationTests {

    @Test
    public void testAddition() {
        final CalculationState state =
                Calculation.calculate(stage(), Operation.ADDITION, 2, 2).await();

        Assertions.assertEquals(4, state.result());
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
        final TestWorld testWorld = TestWorld.start("calculation-test");

        final StatefulTypeRegistry registry = new StatefulTypeRegistry(testWorld.world());

        final StateStore stateStore =
                testWorld.world().stage().actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.emptyList());

        registry.register(new StatefulTypeRegistry.Info(stateStore, CalculationState.class, "StateStore"));
    }

    private Stage stage() {
        return TestWorld.Instance.get().world().stage();
    }

    @AfterEach
    public void tearDown() {
        TestWorld.Instance.get().world().terminate();
    }

}
