package io.vlingo.hike.domain.alarms

import io.vlingo.hike.UnitTest
import io.vlingo.hike.domain.track.Step
import io.vlingo.hike.domain.track.Track
import io.vlingo.hike.domain.track.TrackActor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.random.Random

class AlarmServiceTest: UnitTest() {
    private lateinit var track: Track
    private lateinit var alarmService: AlarmService
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
        alarmService = world().actorFor(AlarmService::class.java, AlarmServiceActor::class.java,
            journal, AlarmsSnapshot(emptyList()), 1L)
    }

    @Test
    fun shouldReadAlarmsGeneratedFromTracks() {
        waitForEvents(3)
        track.step(step)
        track.raiseAlarm()

        appliedEvents()
        val alarms = alarmService.allAlarms().await()

        assertEquals(
            1,
            alarms.journeys.size
        )
    }

    @Test
    fun shouldDiscardAcknowledgeAlarms() {
        waitForEvents(4)
        track.step(step)
        track.raiseAlarm()
        track.notifySafety()

        appliedEvents()
        val alarms = alarmService.allAlarms().await()

        assertEquals(
            0,
            alarms.journeys.size
        )
    }
}