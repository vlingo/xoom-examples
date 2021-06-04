package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.*;
import io.vlingo.xoom.examples.petclinic.model.animaltype.*;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.symbio.store.journal.inmemory.InMemoryJournalActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTypeEntityTests {

    private World world;

    private Journal<String> journal;
    private MockJournalDispatcher dispatcher;
    private SourcedTypeRegistry registry;

    private AnimalType pet;

    @BeforeEach
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setUp(){
        world = World.startWithDefaults("test-es");

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
    public void offerByFactoryMethod(){
        AnimalTypeState animalTypeState = AnimalType.offerTreatmentFor(world.stage(), "Kat").await();
        assertNotNull(animalTypeState.id);
        assertEquals("Kat", animalTypeState.name);
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
        pet.offerTreatmentFor("Owl").await();

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
