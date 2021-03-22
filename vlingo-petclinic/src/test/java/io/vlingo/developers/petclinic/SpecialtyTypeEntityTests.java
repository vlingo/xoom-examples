package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.developers.petclinic.infrastructure.persistence.SpecialtyTypeOfferedAdapter;
import io.vlingo.developers.petclinic.infrastructure.persistence.SpecialtyTypeRenamedAdapter;
import io.vlingo.developers.petclinic.model.specialtytype.*;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecialtyTypeEntityTests {

    private World world;

    private Journal<String> journal;
    private MockJournalDispatcher dispatcher;
    private SourcedTypeRegistry registry;

    private SpecialtyType specialtyType;

    @BeforeEach
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setUp(){
        world = World.startWithDefaults("test-es");

        dispatcher = new MockJournalDispatcher();

        EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(world);

        entryAdapterProvider.registerAdapter(SpecialtyTypeOffered.class, new SpecialtyTypeOfferedAdapter());
        entryAdapterProvider.registerAdapter(SpecialtyTypeRenamed.class, new SpecialtyTypeRenamedAdapter());

        journal = world.actorFor(Journal.class, InMemoryJournalActor.class, Collections.singletonList(dispatcher));

        registry = new SourcedTypeRegistry(world);
        registry.register(new SourcedTypeRegistry.Info(journal, SpecialtyTypeEntity.class, SpecialtyTypeEntity.class.getSimpleName()));

        specialtyType = world.actorFor(SpecialtyType.class, SpecialtyTypeEntity.class, "#1");
    }

    @Test
    public void offer(){
        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        SpecialtyTypeState petState = specialtyType.offer("Behaviour").await();

        assertEquals("#1", petState.id);
        assertEquals("Behaviour", petState.name);

        // this will block until the first event is persisted in the Journal
        assertEquals(1, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt0 = dispatcherAccess.readFrom("appendedAt", 0);
        assertNotNull(appendedAt0);
        assertEquals(SpecialtyTypeOffered.class.getName(), appendedAt0.typeName());
    }

    private SpecialtyTypeState offerExampleSpecialtyType(){
        return specialtyType.offer("Behaviour").await();
    }

    @Test
    public void rename(){
        offerExampleSpecialtyType();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        SpecialtyTypeState petState = specialtyType.rename("Surgery").await();

        assertEquals("#1", petState.id);
        assertEquals("Surgery", petState.name);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(SpecialtyTypeRenamed.class.getName(), appendedAt1.typeName());
    }
}
