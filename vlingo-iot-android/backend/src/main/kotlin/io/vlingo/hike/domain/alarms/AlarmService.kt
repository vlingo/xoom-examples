package io.vlingo.hike.domain.alarms

import io.vlingo.actors.Definition
import io.vlingo.actors.World
import io.vlingo.common.Completes
import io.vlingo.lattice.model.DomainEvent
import io.vlingo.symbio.store.journal.Journal
import java.util.*

data class AlarmStep(val altitude: Double, val longitude: Double, val latitude: Double)
data class AlarmJourney(val journey: UUID, val steps: List<AlarmStep>, val whenStarted: Long)
data class AlarmsSnapshot(val journeys: List<AlarmJourney>)

interface AlarmService {
    data class AlarmDetected(val snapshot: AlarmJourney) : DomainEvent(1)
    data class AlarmAcknowledged(val journey: UUID) : DomainEvent(1)

    fun allAlarms(): Completes<AlarmsSnapshot>

    companion object {
        fun instance(
            world: World,
            journal: Journal<String>,
            alarmInterval: Long
        ): AlarmService {
            return world.stage()
                .actorFor(AlarmService::class.java,
                    Definition.has(AlarmServiceActor::class.java,
                        Definition.parameters(journal, AlarmsSnapshot(emptyList()), alarmInterval)))
        }
    }
}