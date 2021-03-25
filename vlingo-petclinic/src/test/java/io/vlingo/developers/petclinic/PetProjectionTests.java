package io.vlingo.developers.petclinic;

import io.vlingo.actors.*;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.developers.petclinic.infrastructure.*;
import io.vlingo.developers.petclinic.infrastructure.persistence.*;
import io.vlingo.developers.petclinic.model.client.Kind;
import io.vlingo.developers.petclinic.model.client.Name;
import io.vlingo.developers.petclinic.model.client.Owner;
import io.vlingo.developers.petclinic.model.pet.PetKindCorrected;
import io.vlingo.developers.petclinic.model.pet.PetNameChanged;
import io.vlingo.developers.petclinic.model.pet.PetOwnerChanged;
import io.vlingo.developers.petclinic.model.pet.PetRegistered;
import io.vlingo.lattice.model.projection.*;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PetProjectionTests {

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
        StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, PetData.class);
        QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
        projection = world.actorFor(Projection.class, PetProjectionActor.class, stateStore);
    }

    private Projectable createPetRegistered(String id, String name, long birth, String kind, String owner){
        final PetRegistered eventData = new PetRegistered(id, Name.of(name), birth, Kind.of(kind), Owner.of(owner));

        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(PetRegistered.class, 1,
                JsonSerialization.serialized(eventData), 1, Metadata.withObject(eventData));

        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put(id, projectionId);
        return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
    }

    private Projectable createPetNameChanged(String id, String name){
        final PetNameChanged eventData = new PetNameChanged(id, Name.of(name));

        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(PetNameChanged.class, 1,
                JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));

        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put(id, projectionId);
        return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
    }

    private Projectable createPetOwnerChanged(String id, String owner){
        final PetOwnerChanged eventData = new PetOwnerChanged(id, Owner.of(owner));

        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(PetOwnerChanged.class, 1,
                JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));

        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put(id, projectionId);
        return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
    }

    @Test
    public void register(){
        final CountingProjectionControl control = new CountingProjectionControl();

        final AccessSafely access = control.afterCompleting(3);

        projection.projectWith(createPetRegistered("1", "Hedwig", 100L, "Owl", "Potter"), control);
        projection.projectWith(createPetRegistered("2", "Eurasian", 101L, "Owl", "Malfoy"), control);
        projection.projectWith(createPetRegistered("3", "Barn", 102L, "Owl", "Longbottom"), control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(3, confirmations.size());

        assertEquals(1, valueOfProjectionIdFor("1", confirmations));
        assertEquals(1, valueOfProjectionIdFor("2", confirmations));
        assertEquals(1, valueOfProjectionIdFor("3", confirmations));

        CountingReadResultInterest interest = new CountingReadResultInterest();
        AccessSafely interestAccess = interest.afterCompleting(1);
        stateStore.read("1", PetData.class, interest);
        PetData pet = interestAccess.readFrom("item", "1");
        assertEquals("1", pet.id);
        assertEquals("Hedwig", pet.name.value);
        assertEquals(100L, pet.birth);
        assertEquals(0L, pet.death);
        assertEquals("Owl", pet.kind.animalTypeId);
        assertEquals("Potter", pet.owner.clientId);

        interest = new CountingReadResultInterest();
        interestAccess = interest.afterCompleting(1);
        stateStore.read("2", PetData.class, interest);
        pet = interestAccess.readFrom("item", "2");
        assertEquals("2", pet.id);
        assertEquals("Eurasian", pet.name.value);
        assertEquals(101L, pet.birth);
        assertEquals(0L, pet.death);
        assertEquals("Owl", pet.kind.animalTypeId);
        assertEquals("Malfoy", pet.owner.clientId);
    }

    private void registerExamplePet(){
        final CountingProjectionControl control = new CountingProjectionControl();
        final AccessSafely access = control.afterCompleting(3);
        projection.projectWith(createPetRegistered("1", "Hedwig", 100L, "Owl", "Potter"), control);
        projection.projectWith(createPetRegistered("2", "Eurasian", 101L, "Owl", "Malfoy"), control);
        projection.projectWith(createPetRegistered("3", "Barn", 102L, "Owl", "Longbottom"), control);
    }

    @Test
    public void changeName(){
        registerExamplePet();

        final CountingProjectionControl control = new CountingProjectionControl();

        final AccessSafely access = control.afterCompleting(1);

        projection.projectWith(createPetNameChanged("1", "Hedwig v2"), control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(1, confirmations.size());
        assertEquals(1, valueOfProjectionIdFor("1", confirmations));

        final CountingReadResultInterest interest = new CountingReadResultInterest();
        final AccessSafely interestAccess = interest.afterCompleting(1);

        stateStore.read("1", PetData.class, interest);

        final PetData pet = interestAccess.readFrom("item", "1");
        assertEquals("1", pet.id);
        assertEquals("Hedwig v2", pet.name.value);
    }

    @Test
    public void changeOwner(){
        registerExamplePet();

        final CountingProjectionControl control = new CountingProjectionControl();

        final AccessSafely access = control.afterCompleting(1);

        projection.projectWith(createPetOwnerChanged("1", "Black"), control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(1, confirmations.size());
        assertEquals(1, valueOfProjectionIdFor("1", confirmations));

        final CountingReadResultInterest interest = new CountingReadResultInterest();
        final AccessSafely interestAccess = interest.afterCompleting(1);

        stateStore.read("1", PetData.class, interest);

        final PetData pet = interestAccess.readFrom("item", "1");
        assertEquals("1", pet.id);
        assertEquals("Black", pet.owner.clientId);
    }

    @Test
    public void correctKind(){
        registerExamplePet();

        final CountingProjectionControl control = new CountingProjectionControl();
        final AccessSafely access = control.afterCompleting(1);

        final PetKindCorrected eventData = new PetKindCorrected("1", Kind.of("Dog"));
        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(PetKindCorrected.class, 1,
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

        stateStore.read("1", PetData.class, interest);

        final PetData pet = interestAccess.readFrom("item", "1");
        assertEquals("1", pet.id);
        assertEquals("Dog", pet.kind.animalTypeId);
    }

    private int valueOfProjectionIdFor(final String valueText, final Map<String,Integer> confirmations) {
        return confirmations.get(valueToProjectionId.get(valueText));
    }
}
