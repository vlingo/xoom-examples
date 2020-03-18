package io.examples.calculation.domain;

import io.examples.infrastructure.CalculationQueryProvider;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.common.Outcome;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

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
        final World world = TestWorld.start("calculation-test").world();

        final StatefulTypeRegistry registry = new StatefulTypeRegistry(world);

        final StateStore stateStore =
                world.stage().actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.emptyList());

        CalculationQueryProvider.using(world.stage(), stateStore);

        registry.register(new StatefulTypeRegistry.Info(stateStore, CalculationState.class, "StateStore"));

        stateStore.write("0", null, 0, noOpResultInterest());
    }

    private Stage stage() {
        return TestWorld.Instance.get().world().stage();
    }

    @AfterEach
    public void tearDown() {
        TestWorld.Instance.get().world().terminate();
    }

    private StateStore.WriteResultInterest noOpResultInterest() {
        return  new StateStore.WriteResultInterest() {
            @Override
            public <S, C> void writeResultedIn(Outcome<StorageException, Result> outcome, String s, S s1, int i, List<Source<C>> list, Object o) {

            }
        };
    }
}
