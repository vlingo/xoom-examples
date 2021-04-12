package io.vlingo.hike

import com.google.gson.Gson
import io.vlingo.actors.testkit.TestUntil
import io.vlingo.actors.testkit.TestWorld
import io.vlingo.hike.domain.alarms.registerAlarmServiceConsumers
import io.vlingo.hike.domain.track.registerTrackActorConsumers
import io.vlingo.lattice.model.DomainEvent
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry
import io.vlingo.symbio.Entry
import io.vlingo.symbio.State
import io.vlingo.symbio.store.journal.Journal
import io.vlingo.symbio.store.journal.JournalListener
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor
import org.junit.After
import org.junit.Before
import java.util.*

private const val DEFAULT_TIMEOUT: Long = 100

abstract class UnitTest: JournalListener<String> {
    private lateinit var mutexName: String
    private lateinit var testWorld: TestWorld
    protected lateinit var journal: Journal<String>
    private lateinit var registry: SourcedTypeRegistry
    private lateinit var appliedEvents: List<Entry<String>>
    private lateinit var until: TestUntil

    @Before
    internal fun setUpWorld() {
        testWorld = TestWorld.startWithDefaults(javaClass.simpleName)
        journal = Journal.using(testWorld.stage(), InMemoryJournalActor::class.java, this)
        registry = SourcedTypeRegistry(testWorld.world())
        appliedEvents = emptyList()
        until = TestUntil.happenings(1)

        registerTrackActorConsumers(registry, journal)
        registerAlarmServiceConsumers(registry, journal)

        mutexName = UUID.randomUUID().toString()
    }

    @After
    internal fun tearDownWorld() {
        testWorld.terminate()
    }

    fun waitForEvents(a: Int) {
        until = TestUntil.happenings(a)
    }

    fun world() = testWorld.world()

    fun appliedEvents(): List<Entry<String>> {
        until.completesWithin(DEFAULT_TIMEOUT)

        return appliedEvents
    }

    inline fun <reified T: DomainEvent> appliedEventAs(idx: Int): T = Gson().fromJson(appliedEvents()[idx].entryData(), T::class.java)

    override fun appendedAll(entries: MutableList<Entry<String>>?) {
        appliedEvents += entries!!
        entries.forEach { _ -> until.happened() }
    }

    override fun appendedAllWith(entries: MutableList<Entry<String>>?, snapshot: State<String>?) {
        appliedEvents += entries!!
        entries.forEach { _ -> until.happened() }
    }

    override fun appended(entry: Entry<String>?) {
        until.happened()
        appliedEvents += entry!!
    }

    override fun appendedWith(entry: Entry<String>?, snapshot: State<String>?) {
        until.happened()
        appliedEvents += entry!!
    }
}