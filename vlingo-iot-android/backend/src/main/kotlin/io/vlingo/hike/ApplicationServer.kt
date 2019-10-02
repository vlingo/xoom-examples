package io.vlingo.hike

import io.vlingo.actors.World
import io.vlingo.hike.domain.alarms.AlarmService
import io.vlingo.hike.domain.alarms.registerAlarmServiceConsumers
import io.vlingo.hike.domain.track.registerTrackActorConsumers
import io.vlingo.hike.infrastructure.NoopConfigurationInterest
import io.vlingo.hike.infrastructure.NoopJournalListener
import io.vlingo.hike.infrastructure.controller.AlarmController
import io.vlingo.hike.infrastructure.controller.HealthcheckController
import io.vlingo.hike.infrastructure.controller.TrackController
import io.vlingo.http.resource.Resources
import io.vlingo.http.resource.Server
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry
import io.vlingo.symbio.store.DataFormat
import io.vlingo.symbio.store.common.jdbc.Configuration
import io.vlingo.symbio.store.journal.Journal
import io.vlingo.symbio.store.journal.jdbc.postgres.PostgresJournalActor
import org.flywaydb.core.Flyway

class ApplicationServer(val jdbcUrl: String, val username: String, val password: String, val port: Int, val ssl: Boolean = false, val alarmInterval: Long) {
    private lateinit var world: World
    private lateinit var journal: Journal<String>
    private lateinit var registry: SourcedTypeRegistry
    private lateinit var server: Server

    fun start() {
        initWorld()
        initJournal()
        initRegistry()
        initJournalConsumers()
        initServer()
    }

    fun stop() {
        server.stop()
        world.terminate()
    }

    private fun initWorld() {
        world = World.start("hike")
    }

    private fun initJournal() {
        Flyway(
            Flyway.configure()
                .dataSource(jdbcUrl, username, password)
        ).migrate()

        val journalConfiguration = Configuration(
            NoopConfigurationInterest,
            "org.postgresql.Driver",
            DataFormat.Text,
            jdbcUrl,
            "",
            username,
            password,
            ssl,
            "",
            false
        )

        journal = Journal.using(world.stage(), PostgresJournalActor::class.java, NoopJournalListener, journalConfiguration)
    }

    private fun initJournalConsumers() {
        registerTrackActorConsumers(registry, journal)
        registerAlarmServiceConsumers(registry, journal)
    }

    private fun initRegistry() {
        registry = SourcedTypeRegistry(world)
    }

    private fun initServer() {
        val alarmService = AlarmService.instance(world, journal, alarmInterval)
        val resources = Resources.are(
            TrackController(world).asResource(2),
            AlarmController(alarmService).asResource(2),
            HealthcheckController.asResource(1)
        )

        server = Server.startWith(world.stage(),
            resources,
            port,
            io.vlingo.http.resource.Configuration.Sizing.define(),
            io.vlingo.http.resource.Configuration.Timing.define())
    }
}