package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetNameChangedAdapter;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetOwnerChangedAdapter;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetRegisteredAdapter;
import io.vlingo.developers.petclinic.model.client.Kind;
import io.vlingo.developers.petclinic.model.client.Name;
import io.vlingo.developers.petclinic.model.client.Owner;
import io.vlingo.developers.petclinic.model.pet.*;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class PetEntityTests {

    private World world;

    private Journal<String> journal;
    private MockJournalDispatcher dispatcher;
    private SourcedTypeRegistry registry;

    private Pet pet;

    @BeforeEach
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setUp(){
        world = World.startWithDefaults("test-es");

        dispatcher = new MockJournalDispatcher();

        EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(world);

        entryAdapterProvider.registerAdapter(PetRegistered.class, new PetRegisteredAdapter());
        entryAdapterProvider.registerAdapter(PetNameChanged.class, new PetNameChangedAdapter());
        entryAdapterProvider.registerAdapter(PetOwnerChanged.class, new PetOwnerChangedAdapter());

        journal = world.actorFor(Journal.class, InMemoryJournalActor.class, Collections.singletonList(dispatcher));

        registry = new SourcedTypeRegistry(world);
        registry.register(new SourcedTypeRegistry.Info(journal, PetEntity.class, PetEntity.class.getSimpleName()));

        pet = world.actorFor(Pet.class, PetEntity.class, "Pet#1");
    }

    @Test
    public void registerByFactoryMethod(){
        PetState petState = Pet.register(world.stage(), Name.of("Bowtruckle"), 201L, Kind.of("Insect"), Owner.of("Newt"))
                            .await();

        assertEquals("Bowtruckle", petState.name.value);
        assertEquals("Insect", petState.kind.animalTypeId);
        assertEquals(201L, petState.birth);
        assertEquals(0L, petState.death);
        assertEquals("Newt", petState.owner.clientId);
        assertNull(petState.visit);
    }

    @Test
    public void register() {
        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        PetState petState = pet.register(Name.of("Hedwig"), 100L, Kind.of("Owl"), Owner.of("Potter")).await();

        assertEquals("Hedwig", petState.name.value);
        assertEquals("Owl", petState.kind.animalTypeId);
        assertEquals(100L, petState.birth);
        assertEquals(0L, petState.death);
        assertEquals("Potter", petState.owner.clientId);
        assertNull(petState.visit);

        // this will block until the first event is persisted in the Journal
        assertEquals(1, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt0 = dispatcherAccess.readFrom("appendedAt", 0);
        assertNotNull(appendedAt0);
        assertEquals(PetRegistered.class.getName(), appendedAt0.typeName());
    }

    private PetState registerExamplePet(){
        return pet.register(Name.of("Hedwig"), 100L, Kind.of("Owl"), Owner.of("Potter")).await();
    }

    @Test
    public void nameChange() {
        registerExamplePet();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        PetState petState = pet.changeName(Name.of("Mr. Owl")).await();

        assertEquals("Mr. Owl", petState.name.value);
        assertEquals("Owl", petState.kind.animalTypeId);
        assertEquals(100L, petState.birth);
        assertEquals(0L, petState.death);
        assertEquals("Potter", petState.owner.clientId);
        assertNull(petState.visit);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(PetNameChanged.class.getName(), appendedAt1.typeName());
    }

    @Test
    public void ownerChange() {
        registerExamplePet();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        PetState petState = pet.changeOwner(Owner.of("Black")).await();

        assertEquals("Hedwig", petState.name.value);
        assertEquals("Owl", petState.kind.animalTypeId);
        assertEquals(100L, petState.birth);
        assertEquals(0L, petState.death);
        assertEquals("Black", petState.owner.clientId);
        assertNull(petState.visit);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(PetOwnerChanged.class.getName(), appendedAt1.typeName());
    }

    @Test
    public void correctKind() {
        registerExamplePet();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        PetState petState = pet.correctKind(Kind.of("Stork")).await();

        assertEquals("Hedwig", petState.name.value);
        assertEquals("Stork", petState.kind.animalTypeId);
        assertEquals(100L, petState.birth);
        assertEquals(0L, petState.death);
        assertEquals("Potter", petState.owner.clientId);
        assertNull(petState.visit);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(PetKindCorrected.class.getName(), appendedAt1.typeName());
    }

    @Test
    public void recordBirth() {
        registerExamplePet();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        PetState petState = pet.recordBirth(101L).await();

        assertEquals("Hedwig", petState.name.value);
        assertEquals("Owl", petState.kind.animalTypeId);
        assertEquals(101L, petState.birth);
        assertEquals(0L, petState.death);
        assertEquals("Potter", petState.owner.clientId);
        assertNull(petState.visit);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(PetBirthRecorded.class.getName(), appendedAt1.typeName());
    }

    @Test
    public void recordDeath() {
        registerExamplePet();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        PetState petState = pet.recordDeath(201L).await();

        assertEquals("Hedwig", petState.name.value);
        assertEquals("Owl", petState.kind.animalTypeId);
        assertEquals(100L, petState.birth);
        assertEquals(201L, petState.death);
        assertEquals("Potter", petState.owner.clientId);
        assertNull(petState.visit);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(PetDeathRecorded.class.getName(), appendedAt1.typeName());
    }
}
