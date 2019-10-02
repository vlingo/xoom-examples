package com.example.vlingohike.domain.journey

import com.example.vlingohike.ActorTest
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.google.gson.Gson
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.random.Random

class JourneyTest: ActorTest() {
    private lateinit var journey: Journey
    private lateinit var journeyId: UUID
    private lateinit var step: Step

    @get:Rule
    val wireMockRule = WireMockRule(8080)

    @Before
    fun setUp() {
        step = Step(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        journeyId = UUID.randomUUID()
        journey = world().actorFor(Journey::class.java, JourneyActor::class.java, journeyId, "localhost:8080")
    }

    @Test
    fun shouldUpdateTheBackendOnANewStep() {
        stubFor(put(urlMatching(".*/journey/$journeyId/step.*"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
            ))

        journey.step(step)

        verify(putRequestedFor(urlEqualTo("/journey/$journeyId/step"))
            .withRequestBody(equalToJson(Gson().toJson(step))))
    }

    @Test
    fun shouldRaiseAnAlarm() {
        stubFor(put(urlMatching(".*/journey/$journeyId/alarm.*"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
            ))

        journey.inDanger()

        verify(putRequestedFor(urlEqualTo("/journey/$journeyId/alarm")))
    }

    @Test
    fun shouldRaiseAcknowledgeAnAlarm() {
        stubFor(delete(urlMatching(".*/journey/$journeyId/alarm.*"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withStatusMessage("Ok")
                    .withHeader("Content-Type", "application/json")
            ))

        journey.safe()

        verify(deleteRequestedFor(urlEqualTo("/journey/$journeyId/alarm")))
    }
}