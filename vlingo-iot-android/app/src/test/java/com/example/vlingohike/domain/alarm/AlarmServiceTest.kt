package com.example.vlingohike.domain.alarm

import com.example.vlingohike.ActorTest
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import com.google.gson.Gson
import io.vlingo.actors.testkit.TestUntil
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

class AlarmServiceTest: ActorTest(), AlarmListener {
    private val TIMEOUT = 500L

    private lateinit var snapshot: AlarmsSnapshot
    private lateinit var service: AlarmService
    private lateinit var until: TestUntil
    private lateinit var receivedAlarms: List<Alarm>
    private lateinit var receivedAcknowledges: List<Acknowledge>
    private lateinit var journeyId: UUID
    private lateinit var step: AlarmStep
    private var whenHappened: Long = 0L

    @get:Rule
    val wireMockRule = WireMockRule(8080)

    @Before
    fun setUp() {
        receivedAlarms = emptyList()
        receivedAcknowledges = emptyList()
        journeyId = UUID.randomUUID()
        step = AlarmStep(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        whenHappened = Date().time
        until = TestUntil.happenings(1)
        snapshot = AlarmsSnapshot(
            listOf(
                AlarmJourney(
                    journeyId, listOf(step), whenHappened
                )
            )
        )

        val response = Gson().toJson(snapshot)
        stubFor(get("/alarms")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("X-Correlation-ID", "{{request.headers.X-Correlation-ID}}")
                .withHeader("Content-Type", "application/json")
                .withHeader("Content-Length", response.length.toString())
                .withBody(response)
                .withTransformers("response-template")))
    }

    @Test
    fun shouldNotifyANewAlarmWhenThereWereNoAlarms() {
        service = world().actorFor(AlarmService::class.java, AlarmServiceActor::class.java, this, 1L, "localhost:8080", emptyList<Alarm>())
        service.run()
        until.completesWithin(TIMEOUT)

        verify(getRequestedFor(urlEqualTo("/alarms")))

        assertEquals(snapshot.journeys[0].journey, receivedAlarms[0].journey)
        assertEquals(snapshot.journeys[0].steps[0], receivedAlarms[0].step)
    }

    @Test
    fun shouldNotifyAcknowledgeWhenAnAlarmIsNotThereAnymore() {
        val alarm = Alarm(UUID.randomUUID(), AlarmStep(0.0, 0.0, 0.0), whenHappened)
        service = world().actorFor(AlarmService::class.java, AlarmServiceActor::class.java, this, 1L, "localhost:8080", listOf(alarm))

        service.run()
        until.completesWithin(TIMEOUT)

        verify(getRequestedFor(urlEqualTo("/alarms")))

        assertEquals(alarm.journey, receivedAcknowledges[0].journey)
        assertEquals(snapshot.journeys[0].steps[0], receivedAlarms[0].step)
    }

    override fun onAlarm(alarm: Alarm) {
        until.happened()
        receivedAlarms += alarm
    }

    override fun onAcknowledge(acknowledge: Acknowledge) {
        until.happened()
        receivedAcknowledges += acknowledge
    }
}