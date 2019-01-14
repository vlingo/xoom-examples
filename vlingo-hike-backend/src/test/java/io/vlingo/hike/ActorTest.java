package io.vlingo.hike;

import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.hike.infrastructure.SourcedRegistration;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;
import org.junit.After;
import org.junit.Before;

public abstract class ActorTest {
    private TestWorld testWorld;
    private InMemoryJournalListener journalListener;
    private Journal<String> journal;
    private SourcedTypeRegistry registry;

    @Before
    public final void setUpTestWorld() throws Exception {
        testWorld = TestWorld.startWithDefaults(getClass().getSimpleName());
        journalListener = new InMemoryJournalListener();
        journal = world().actorFor(Journal.class, InMemoryJournalActor.class, journalListener).actor();
        registry = new SourcedTypeRegistry(world().world());
        SourcedRegistration.registerAllWith(registry, journal);
    }

    @After
    public void tearDownTestWorld() throws Exception {
        testWorld.terminate();
    }

    protected final TestWorld world() {
        return testWorld;
    }

    protected final Journal<String> journal() {
        return journal;
    }

    protected final Entry<String> entry(int idx) {
        return journal().journalReader(getClass().getSimpleName()).await(500)
                .readNext(100).await(500)
                .entries.get(idx);
    }
}
