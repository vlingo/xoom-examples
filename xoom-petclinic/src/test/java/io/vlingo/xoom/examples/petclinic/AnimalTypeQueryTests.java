package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.AnimalTypeQueries;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.AnimalTypeQueriesActor;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTypeQueryTests {

    private World world;
    private StateStore stateStore;
    private AnimalTypeQueries animalTypeQueries;

    @BeforeEach
    public void setUp(){
        world = World.startWithDefaults("test-state-store-query");
        stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class,
                                    Collections.singletonList(new NoOpDispatcher()));
        StatefulTypeRegistry.registerAll(world, stateStore, AnimalTypeData.class);
        animalTypeQueries = world.actorFor(AnimalTypeQueries.class, AnimalTypeQueriesActor.class, stateStore);
    }

    @Test
    public void animalTypeOf(){
        stateStore.write("1", AnimalTypeData.from("1", "Dog"), 1, new NoopWriteResultInterest());
        stateStore.write("2", AnimalTypeData.from("2", "Cat"), 1, new NoopWriteResultInterest());
        AnimalTypeData animalTypeData = animalTypeQueries.animalTypeOf("1").await();
        assertEquals("1", animalTypeData.id);
        assertEquals("Dog", animalTypeData.name);

        animalTypeData = animalTypeQueries.animalTypeOf("2").await();
        assertEquals("2", animalTypeData.id);
        assertEquals("Cat", animalTypeData.name);
    }

    @Test
    public void animalTypes(){
        stateStore.write("1", AnimalTypeData.from("1", "Dog"), 1, new NoopWriteResultInterest());
        stateStore.write("2", AnimalTypeData.from("2", "Cat"), 1, new NoopWriteResultInterest());
        Collection<AnimalTypeData> animalTypes = animalTypeQueries.animalTypes().await();
        assertEquals(2, animalTypes.size());
    }

    @Test
    public void animalTypes2(){
        stateStore.write("1", AnimalTypeData.from("1", "Dog"), 1, new NoopWriteResultInterest());
        Collection<AnimalTypeData> animalTypes = animalTypeQueries.animalTypes().await();
        assertEquals(1, animalTypes.size());
        AnimalTypeData animalTypeData = animalTypes.stream().findFirst().orElseThrow(RuntimeException::new);
        assertEquals("1", animalTypeData.id);
        assertEquals("Dog", animalTypeData.name);
    }
}
