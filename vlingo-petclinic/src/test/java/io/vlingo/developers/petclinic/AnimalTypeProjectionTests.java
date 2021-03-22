package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.developers.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.developers.petclinic.infrastructure.persistence.AnimalTypeProjectionActor;
import io.vlingo.developers.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeRenamed;
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeTreatmentOffered;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.Projection;
import io.vlingo.lattice.model.projection.TextProjectable;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTypeProjectionTests {

    private World world;
    private StateStore stateStore;
    private Projection projection;
    private Map<String, String> valueToProjectionId;

    @BeforeEach
    public void setUp(){
        world = World.startWithDefaults("test-state-store-projection");
        NoOpDispatcher dispatcher = new NoOpDispatcher();
        valueToProjectionId = new ConcurrentHashMap<>();
        stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
        StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, AnimalTypeData.class);
        QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
        projection = world.actorFor(Projection.class, AnimalTypeProjectionActor.class, stateStore);
    }

    private Projectable createPetOwnerChanged(String id, String name){
        final AnimalTypeTreatmentOffered eventData = new AnimalTypeTreatmentOffered(id, name);

        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(AnimalTypeTreatmentOffered.class, 1,
                JsonSerialization.serialized(eventData), 1, Metadata.withObject(eventData));

        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put(id, projectionId);
        return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
    }

    @Test
    public void register(){
        final CountingProjectionControl control = new CountingProjectionControl();

        final AccessSafely access = control.afterCompleting(2);

        projection.projectWith(createPetOwnerChanged("1", "Owl"), control);
        projection.projectWith(createPetOwnerChanged("2", "Dog"), control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(2, confirmations.size());

        assertEquals(1, valueOfProjectionIdFor("1", confirmations));
        assertEquals(1, valueOfProjectionIdFor("2", confirmations));

        CountingReadResultInterest interest = new CountingReadResultInterest();
        AccessSafely interestAccess = interest.afterCompleting(1);
        stateStore.read("1", AnimalTypeData.class, interest);
        AnimalTypeData animalType = interestAccess.readFrom("item", "1");
        assertEquals("1", animalType.id);
        assertEquals("Owl", animalType.name);

        interest = new CountingReadResultInterest();
        interestAccess = interest.afterCompleting(1);
        stateStore.read("2", AnimalTypeData.class, interest);
        animalType = interestAccess.readFrom("item", "2");
        assertEquals("2", animalType.id);
        assertEquals("Dog", animalType.name);
    }

    @Test
    public void changeName(){
        register();

        final CountingProjectionControl control = new CountingProjectionControl();
        final AccessSafely access = control.afterCompleting(1);

        final AnimalTypeRenamed eventData = new AnimalTypeRenamed("1", "Cat");
        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(AnimalTypeRenamed.class, 1,
                JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));
        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put("1", projectionId);
        Projectable p = new TextProjectable(null, Collections.singletonList(textEntry), projectionId);

        projection.projectWith(p, control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(1, confirmations.size());
        assertEquals(1, valueOfProjectionIdFor("1", confirmations));

        final CountingReadResultInterest interest = new CountingReadResultInterest();
        final AccessSafely interestAccess = interest.afterCompleting(1);

        stateStore.read("1", AnimalTypeData.class, interest);

        final AnimalTypeData pet = interestAccess.readFrom("item", "1");
        assertEquals("1", pet.id);
        assertEquals("Cat", pet.name);
    }

    private int valueOfProjectionIdFor(final String valueText, final Map<String,Integer> confirmations) {
        return confirmations.get(valueToProjectionId.get(valueText));
    }
}
