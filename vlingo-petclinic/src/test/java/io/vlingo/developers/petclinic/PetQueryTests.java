package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.developers.petclinic.infrastructure.KindData;
import io.vlingo.developers.petclinic.infrastructure.NameData;
import io.vlingo.developers.petclinic.infrastructure.OwnerData;
import io.vlingo.developers.petclinic.infrastructure.PetData;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetQueries;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetQueriesActor;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PetQueryTests {

    private static final StateStore.WriteResultInterest NOOP_WRI = new NoopWriteResultInterest();

    private World world;
    private StateStore stateStore;
    private PetQueries queries;

    @BeforeEach
    public void setUp(){
        world = World.startWithDefaults("test-state-store-query");
        stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class,
                                    Collections.singletonList(new NoOpDispatcher()));
        StatefulTypeRegistry.registerAll(world, stateStore, PetData.class);
        queries = world.actorFor(PetQueries.class, PetQueriesActor.class, stateStore);
    }

    @Test
    public void petOfEmptyResult(){
        PetData item = queries.petOf("1").await();
        assertEquals("", item.id);
    }

    @Test
    public void petOf(){
        stateStore.write("1", PetData.from("1", NameData.of("Hedwig"), 101L, 0L, KindData.of("Owl"),
                            OwnerData.of("Potter"), null), 1, NOOP_WRI);
        stateStore.write("2", PetData.from("2", NameData.of("Crookshanks"), 102L, 0L, KindData.of("Cat"),
                            OwnerData.of("Granger"), null), 1, NOOP_WRI);
        PetData item = queries.petOf("1").await();
        assertEquals("1", item.id);
        assertEquals("Hedwig", item.name.value);
        assertEquals("Owl", item.kind.animalTypeId);
        assertEquals("Potter", item.owner.clientId);
        assertEquals(101L, item.birth);
        assertEquals(0L, item.death);
        assertNull(item.visit);

        item = queries.petOf("2").await();
        assertEquals("2", item.id);
        assertEquals("Crookshanks", item.name.value);
    }

    @Test
    public void pets(){
        stateStore.write("1", PetData.from("1", NameData.of("Hedwig"), 101L, 0L, KindData.of("Owl"),
                            OwnerData.of("Potter"), null), 1, NOOP_WRI);
        stateStore.write("2", PetData.from("2", NameData.of("Crookshanks"), 102L, 0L, KindData.of("Cat"),
                            OwnerData.of("Granger"), null), 1, NOOP_WRI);
        Collection<PetData> animalTypes = queries.pets().await();
        assertEquals(2, animalTypes.size());
    }

    @Test
    public void pets2(){
        stateStore.write("1", PetData.from("1", NameData.of("Hedwig"), 101L, 0L, KindData.of("Owl"),
                            OwnerData.of("Potter"), null), 1, NOOP_WRI);
        Collection<PetData> animalTypes = queries.pets().await();
        assertEquals(1, animalTypes.size());
        PetData item = animalTypes.stream().findFirst().orElseThrow(RuntimeException::new);
        assertEquals("1", item.id);
        assertEquals("Hedwig", item.name.value);
        assertEquals("Owl", item.kind.animalTypeId);
        assertEquals("Potter", item.owner.clientId);
        assertEquals(101L, item.birth);
        assertEquals(0L, item.death);
        assertNull(item.visit);
    }
}
