package io.vlingo.hike.domain.alarms

import com.google.gson.Gson
import io.vlingo.common.Completes
import io.vlingo.common.Scheduled
import io.vlingo.hike.domain.track.Track
import io.vlingo.hike.domain.register
import io.vlingo.hike.domain.withConsumer
import io.vlingo.lattice.model.sourcing.EventSourced
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry
import io.vlingo.symbio.Entry
import io.vlingo.symbio.store.journal.Journal
import io.vlingo.symbio.store.journal.JournalReader
import java.time.ZoneOffset

class AlarmServiceActor(
    journal: Journal<String>,
    private var snapshots: AlarmsSnapshot,
    pollingInterval: Long
) : EventSourced(), AlarmService, Scheduled<Any> {
    private val gson: Gson = Gson()
    private val reader: JournalReader<Entry<String>> =
        journal.journalReader<Entry<String>>("alarms-journal-reader").await()

    init {
        scheduler().schedule(selfAs(Scheduled::class.java) as Scheduled<Any>, null, 0, pollingInterval)
    }

    override fun streamName() = "alarms"

    override fun allAlarms(): Completes<AlarmsSnapshot> {
        return completes<AlarmsSnapshot>().with(snapshots)
    }

    fun whenAlarmDetected(alarmDetected: AlarmService.AlarmDetected) {
        snapshots = snapshots.copy(journeys = snapshots.journeys.filter { it.journey != alarmDetected.snapshot.journey } + alarmDetected.snapshot)
    }

    fun whenAlarmAcknowledged(alarmAcknowledged: AlarmService.AlarmAcknowledged) {
        snapshots = snapshots.copy(journeys = snapshots.journeys.filter { it.journey != alarmAcknowledged.journey })
    }

    override fun intervalSignal(scheduled: Scheduled<Any>?, data: Any?) {
        reader.readNext(100).andThenConsume { events ->
            events.forEach { event ->
                if (event.type().endsWith("AlarmRaised")) {
                    val alarmRaised = gson.fromJson(event.entryData(), Track.AlarmRaised::class.java)
                    apply(
                        AlarmService.AlarmDetected(
                            AlarmJourney(
                                alarmRaised.track,
                                alarmRaised.steps.takeLast(1).map { AlarmStep(it.altitude, it.longitude, it.latitude) },
                                alarmRaised.happened.toInstant(ZoneOffset.UTC).toEpochMilli()
                            )
                        )
                    )
                }

                if (event.type().endsWith("SafetyNotified")) {
                    val safetyNotified = gson.fromJson(event.entryData(), Track.SafetyNotified::class.java)
                    apply(
                        AlarmService.AlarmAcknowledged(safetyNotified.track)
                    )
                }
            }
        }
    }
}


fun registerAlarmServiceConsumers(registry: SourcedTypeRegistry, journal: Journal<String>) {
    registry.register<AlarmServiceActor>(journal)
        .withConsumer(AlarmServiceActor::whenAlarmDetected)
        .withConsumer(AlarmServiceActor::whenAlarmAcknowledged)
}