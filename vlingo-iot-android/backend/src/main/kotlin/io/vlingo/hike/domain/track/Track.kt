package io.vlingo.hike.domain.track

import io.vlingo.actors.World
import io.vlingo.common.Completes
import io.vlingo.hike.infrastructure.actor
import io.vlingo.hike.infrastructure.addressOfString
import io.vlingo.lattice.model.DomainEvent
import java.time.LocalDateTime
import java.util.*

data class Step(val altitude: Double, val latitude: Double, val longitude: Double) {
    companion object {
        fun inPosition(altitude: Double, latitude: Double, longitude: Double): Step =
                Step(altitude, latitude, longitude)
    }
}

data class TrackSnapshot(val steps: List<Step>, val alarm: Boolean)
interface Track {
    data class StepTracked(val track: UUID, val step: Step, val happened: LocalDateTime = LocalDateTime.now())
        : DomainEvent(1)
    data class AlarmRaised(val track: UUID, val steps: List<Step>, val happened: LocalDateTime = LocalDateTime.now())
        : DomainEvent(1)
    data class SafetyNotified(val track: UUID) : DomainEvent(1)

    fun step(step: Step)
    fun raiseAlarm()
    fun notifySafety()
    fun journey(): Completes<TrackSnapshot>

    companion object {
        fun by(world: World, id: UUID): Completes<Track> {
            val address = world.addressOfString(id.toString())
            return world.actor<Track, TrackActor>(arrayOf(id, emptyList<Step>(), false), address)
        }
    }
}