package io.vlingo.hike

import io.vlingo.actors.Stage
import io.vlingo.actors.testkit.TestUntil
import io.vlingo.actors.testkit.TestWorld
import io.vlingo.hike.infrastructure.actors.actorFor
import io.vlingo.hike.infrastructure.registerSources
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry
import io.vlingo.symbio.Entry
import io.vlingo.symbio.State
import io.vlingo.symbio.store.journal.Journal
import io.vlingo.symbio.store.journal.JournalReader
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor
import org.junit.After
import org.junit.Before

abstract class ActorTest {
    private lateinit var testWorld: TestWorld
    private lateinit var journalListener: InMemoryJournalListener
    private lateinit var journal: Journal<String>
    private lateinit var registry: SourcedTypeRegistry
    private lateinit var reader: JournalReader<String>

    @Before
    fun setUpTestWorld() {
        testWorld = TestWorld.startWithDefaults(javaClass.simpleName)
        journalListener = InMemoryJournalListener()
        journal = testWorld.stage().actorFor<Journal<String>, InMemoryJournalActor<String, State.TextState>>(journalListener)
        registry = SourcedTypeRegistry(testWorld.world())

        registerSources(registry, journal)
    }

    @After
    fun tearDownTestWorld() {
        testWorld.terminate()
    }

    protected fun stage(): Stage {
        return testWorld.stage()
    }

    protected fun withExpectedEvents(number: Int, fn: () -> Unit): List<Entry<String>> {
        expectedEvents(number)
        fn()
        waitForEvents()
        return events()
    }

    private fun events(): List<Entry<String>> {
        reader = journal.journalReader(javaClass.simpleName).await(500)
        val stream = reader.readNext(10).await(500)
        return stream.entries
    }

    private fun expectedEvents(number: Int) {
        journalListener.until = TestUntil.happenings(number)
    }

    private fun waitForEvents() {
        journalListener.until.completesWithin(1000000)
    }
}