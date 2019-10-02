package io.vlingo.hike.e2e

import io.restassured.RestAssured.given
import io.vlingo.hike.Specification
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import java.util.*

class HikerSpecification : Specification() {
    private lateinit var trackId: UUID

    @Before
    fun setUp() {
        trackId = UUID.randomUUID()
    }

    @Test
    fun shouldBeAbleToSeeTheirTrack() {
        given()
            .body("""{ "altitude": 0.1, "latitude": 0.1, "longitude": 0.1 }""")
            .put("/journey/$trackId/step")
            .then()
            .statusCode(200)

        given()
            .get("/journey/$trackId")
            .then()
            .statusCode(200)
            .body(containsString(""""altitude":0.1"""))
            .body(containsString(""""latitude":0.1"""))
            .body(containsString(""""longitude":0.1"""))
    }

    @Test
    fun shouldBeAbleToSeeAlarmedTracks() {
        given()
            .body("""{ "altitude": 0.1, "latitude": 0.1, "longitude": 0.1 }""")
            .put("/journey/$trackId/step")
            .then()
            .statusCode(200)

        given()
            .put("/journey/$trackId/alarm")
            .then()
            .statusCode(200)

        given()
            .get("/alarms")
            .then()
            .statusCode(200)
            .body(containsString(""""$trackId"""))
            .body(containsString(""""altitude":0.1"""))
            .body(containsString(""""latitude":0.1"""))
            .body(containsString(""""longitude":0.1"""))
    }

    @Test
    fun shouldNotSeeAcknowledgedAlarms() {
        given()
            .body("""{ "altitude": 0.1, "latitude": 0.1, "longitude": 0.1 }""")
            .put("/journey/$trackId/step")
            .then()
            .statusCode(200)

        given()
            .put("/journey/$trackId/alarm")
            .then()
            .statusCode(200)

        given()
            .delete("/journey/$trackId/alarm")
            .then()
            .statusCode(200)

        given()
            .get("/alarms")
            .then()
            .statusCode(200)
            .body("journeys.size()", equalTo(0))
    }
}