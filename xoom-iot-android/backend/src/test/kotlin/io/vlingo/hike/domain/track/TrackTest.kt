package io.vlingo.hike.domain.track

import io.vlingo.hike.UnitTest
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.random.Random

class TrackTest : UnitTest() {
    private lateinit var track: Track
    private lateinit var step: Step
    private lateinit var trackId: UUID

    @Before
    fun setUp() {
        trackId = UUID.randomUUID()
        track = world().actorFor(Track::class.java, TrackActor::class.java, trackId, emptyList<Step>(), false)

        val altitude = Random.nextDouble()
        val latitude = Random.nextDouble()
        val longitude = Random.nextDouble()

        step = Step.inPosition(altitude, latitude, longitude)
    }

    @Test
    fun shouldAddANewTrack() {
        waitForEvents(1)

        track.step(step)
        assertEquals(
            step,
            appliedEventAs<Track.StepTracked>(0).step
        )
    }

    @Test
    fun shouldRaiseAnAlarmWithAllTheStepsTracked() {
        waitForEvents(2)

        track.step(step)
        track.raiseAlarm()

        assertEquals(
            listOf(step),
            appliedEventAs<Track.AlarmRaised>(1).steps
        )
    }

    @Test
    fun shouldRaiseAnAlarmOnEachStepWhenTheAlarmWasFirstRaised() {
        waitForEvents(3)

        track.raiseAlarm()
        track.step(step)

        assertEquals(
            listOf(step),
            appliedEventAs<Track.AlarmRaised>(2).steps
        )
    }

    @Test
    fun shouldNotifySafetyWhenAnAlarmWasRaised() {
        waitForEvents(2)

        track.raiseAlarm()
        track.notifySafety()

        assertEquals(
            trackId,
            appliedEventAs<Track.AlarmRaised>(0).track
        )

        assertEquals(
            trackId,
            appliedEventAs<Track.SafetyNotified>(1).track
        )
    }

    @Test
    fun shouldNotNotifyTwiceSafety() {
        waitForEvents(2)

        track.raiseAlarm()
        track.notifySafety()
        track.notifySafety()

        assertEquals(2, appliedEvents().size)
    }

    @Test
    fun shouldReturnTheListOfSteps() {
        track.step(step)
        val snapshot = track.journey().await()

        assertEquals(listOf(step), snapshot.steps)
        assertEquals(false, snapshot.alarm)
    }
}