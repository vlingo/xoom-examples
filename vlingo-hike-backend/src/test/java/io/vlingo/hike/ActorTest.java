package io.vlingo.hike;

import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.hike.infrastructure.SourcedRegistration;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.JournalReader;
import io.vlingo.symbio.store.journal.Stream;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public abstract class ActorTest {
    private TestWorld testWorld;
    private InMemoryJournalListener journalListener;
    private Journal<String> journal;
    private SourcedTypeRegistry registry;
    private JournalReader<String> reader;

    @Before
    @SuppressWarnings({"unchecked"})
    public final void setUpTestWorld() {
        testWorld = TestWorld.startWithDefaults(getClass().getSimpleName());
        journalListener = new InMemoryJournalListener();
        journal = world().actorFor(Journal.class, InMemoryJournalActor.class, journalListener).actor();
        registry = new SourcedTypeRegistry(world().world());
        reader = journal.journalReader(getClass().getSimpleName()).await(500);
        SourcedRegistration.registerAllWith(registry, journal);
    }

    @After
    public void tearDownTestWorld() {
        testWorld.terminate();
    }

    protected final TestWorld world() {
        return testWorld;
    }

    protected final List<Entry<String>> entries() {
        reader.rewind();
        Stream<String> stream = reader.readNext(10).await(500);
        return stream.entries;
    }
}
