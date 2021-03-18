package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.developers.petclinic.infrastructure.persistence.*;
import io.vlingo.developers.petclinic.model.animaltype.*;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTypeEntityTests {

    private World world;
    private TestWorld testWorld;

    private Journal<String> journal;
    private MockJournalDispatcher dispatcher;
    private SourcedTypeRegistry registry;

    private AnimalType pet;

    @BeforeEach
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setUp(){
        testWorld = TestWorld.startWithDefaults("test-es");

        world = testWorld.world();

        dispatcher = new MockJournalDispatcher();

        EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(world);

        entryAdapterProvider.registerAdapter(AnimalTypeTreatmentOffered.class, new AnimalTypeTreatmentOfferedAdapter());
        entryAdapterProvider.registerAdapter(AnimalTypeRenamed.class, new AnimalTypeRenamedAdapter());

        journal = world.actorFor(Journal.class, InMemoryJournalActor.class, Collections.singletonList(dispatcher));

        registry = new SourcedTypeRegistry(world);
        registry.register(new SourcedTypeRegistry.Info(journal, AnimalTypeEntity.class, AnimalTypeEntity.class.getSimpleName()));

        pet = world.actorFor(AnimalType.class, AnimalTypeEntity.class, "#1");
    }

    @Test
    public void offer(){
        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        AnimalTypeState petState = pet.offerTreatmentFor("Owl").await();

        assertEquals("#1", petState.id);
        assertEquals("Owl", petState.name);

        // this will block until the first event is persisted in the Journal
        assertEquals(1, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt0 = dispatcherAccess.readFrom("appendedAt", 0);
        assertNotNull(appendedAt0);
        assertEquals(AnimalTypeTreatmentOffered.class.getName(), appendedAt0.typeName());
    }

    @Test
    public void rename(){
        offer();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        AnimalTypeState petState = pet.rename("Dog").await();

        assertEquals("#1", petState.id);
        assertEquals("Dog", petState.name);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(AnimalTypeRenamed.class.getName(), appendedAt1.typeName());
    }
}
