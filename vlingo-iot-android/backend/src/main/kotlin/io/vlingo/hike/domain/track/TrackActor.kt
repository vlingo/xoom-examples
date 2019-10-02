package io.vlingo.hike.domain.track

import io.vlingo.common.Completes
import io.vlingo.hike.domain.register
import io.vlingo.hike.domain.withConsumer
import io.vlingo.lattice.model.sourcing.EventSourced
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry
import io.vlingo.symbio.store.journal.Journal
import java.time.LocalDateTime
import java.util.*

class TrackActor(
    val id: UUID,
    var steps: List<Step>,
    var alarmRaised: Boolean
): EventSourced(), Track {
    override fun streamName() = "tracks/$id"

    override fun step(step: Step) {
        apply(Track.StepTracked(id, step))
        logger().log("Step tracked for track $id: $step")

        if (alarmRaised) {
            logger().log("Alarm raised for track $id during step $step")
            apply(Track.AlarmRaised(id, steps + step))
        }
    }

    override fun raiseAlarm() {
        logger().log("Alarm raised for track $id")
        apply(Track.AlarmRaised(id, steps))
    }

    override fun notifySafety() {
        if (alarmRaised) {
            logger().log("Alarm acknowledged for track $id")
            apply(Track.SafetyNotified(id))
        }
    }

    override fun journey(): Completes<TrackSnapshot> {
        return completes<TrackSnapshot>().with(TrackSnapshot(steps, alarmRaised))
    }

    fun whenStepTracked(event: Track.StepTracked) {
        this.steps += event.step
    }

    fun whenAlarmRaised(event: Track.AlarmRaised) {
        this.alarmRaised = true
    }

    fun whenSafetyNotified(event: Track.SafetyNotified) {
        this.alarmRaised = false
    }
}

fun registerTrackActorConsumers(registry: SourcedTypeRegistry, journal: Journal<String>) {
    registry.register<TrackActor>(journal)
        .withConsumer(TrackActor::whenStepTracked)
        .withConsumer(TrackActor::whenAlarmRaised)
        .withConsumer(TrackActor::whenSafetyNotified)
}