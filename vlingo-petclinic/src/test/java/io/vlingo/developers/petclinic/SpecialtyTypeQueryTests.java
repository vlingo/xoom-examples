package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.developers.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.developers.petclinic.infrastructure.persistence.SpecialtyTypeQueries;
import io.vlingo.developers.petclinic.infrastructure.persistence.SpecialtyTypeQueriesActor;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpecialtyTypeQueryTests {

    private static final StateStore.WriteResultInterest NOOP_WRI = new NoopWriteResultInterest();

    private World world;
    private StateStore stateStore;
    private SpecialtyTypeQueries queries;

    @BeforeEach
    public void setUp(){
        world = World.startWithDefaults("test-state-store-query");
        stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class,
                                    Collections.singletonList(new NoOpDispatcher()));
        StatefulTypeRegistry.registerAll(world, stateStore, SpecialtyTypeData.class);
        queries = world.actorFor(SpecialtyTypeQueries.class, SpecialtyTypeQueriesActor.class, stateStore);
    }

    @Test
    public void specialtyOfEmptyResult(){
        SpecialtyTypeData item = queries.specialtyTypeOf("1").await();
        assertEquals("", item.id);
    }

    @Test
    public void specialtyOf(){
        stateStore.write("1", SpecialtyTypeData.from("1", "Surgery"), 1, NOOP_WRI);
        stateStore.write("2", SpecialtyTypeData.from("2", "Behaviour"), 1, NOOP_WRI);
        SpecialtyTypeData animalTypeData = queries.specialtyTypeOf("1").await();
        assertEquals("1", animalTypeData.id);
        assertEquals("Surgery", animalTypeData.name);

        animalTypeData = queries.specialtyTypeOf("2").await();
        assertEquals("2", animalTypeData.id);
        assertEquals("Behaviour", animalTypeData.name);
    }

    @Test
    public void specialties(){
        stateStore.write("1", SpecialtyTypeData.from("1", "Surgery"), 1, NOOP_WRI);
        stateStore.write("2", SpecialtyTypeData.from("2", "Behaviour"), 1, NOOP_WRI);
        Collection<SpecialtyTypeData> animalTypes = queries.specialtyTypes().await();
        assertEquals(2, animalTypes.size());
    }

    @Test
    public void specialties2(){
        stateStore.write("1", SpecialtyTypeData.from("1", "Surgery"), 1, NOOP_WRI);
        Collection<SpecialtyTypeData> animalTypes = queries.specialtyTypes().await();
        assertEquals(1, animalTypes.size());
        SpecialtyTypeData animalTypeData = animalTypes.stream().findFirst().orElseThrow(RuntimeException::new);
        assertEquals("1", animalTypeData.id);
        assertEquals("Surgery", animalTypeData.name);
    }
}
