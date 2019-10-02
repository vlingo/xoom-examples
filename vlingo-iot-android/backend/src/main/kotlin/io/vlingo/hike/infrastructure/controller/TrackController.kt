package io.vlingo.hike.infrastructure.controller

import io.vlingo.actors.World
import io.vlingo.common.Completes
import io.vlingo.hike.domain.track.Step
import io.vlingo.hike.domain.track.Track
import io.vlingo.http.Response
import io.vlingo.http.ResponseHeader
import io.vlingo.http.resource.ResourceBuilder.*
import io.vlingo.http.resource.serialization.JsonSerialization.serialized
import java.util.*

data class TrackStepCommand(val altitude: Double, val longitude: Double, val latitude: Double)
class TrackController(val world: World) {
    fun asResource(pool: Int) = resource("journey", pool,
        put("/journey/{trackId}/step")
            .param(String::class.java)
            .body(TrackStepCommand::class.java)
            .handle(this::doTrackStep)
            .onError(this::onError),
        get("/journey/{trackId}")
            .param(String::class.java)
            .handle(this::seeJourney)
            .onError(this::onError),
        put("/journey/{trackId}/alarm")
            .param(String::class.java)
            .handle(this::raiseAlarm)
            .onError(this::onError),
        delete("/journey/{trackId}/alarm")
            .param(String::class.java)
            .handle(this::notifySafety)
            .onError(this::onError))

    private fun doTrackStep(trackId: String, command: TrackStepCommand): Completes<Response> {
        return Track.by(world, UUID.fromString(trackId))
            .andThenConsume { track -> track.step(Step.inPosition(command.altitude, command.latitude, command.longitude)) }
            .andThen {
                Response.of(Response.Status.Ok)
                    .include(ResponseHeader.contentType("application/json"))
            }
    }

    private fun seeJourney(trackId: String): Completes<Response> {
        return Track.by(world, UUID.fromString(trackId))
            .andThenTo { it.journey() }
            .andThen {
                Response.of(Response.Status.Ok, serialized(it))
                    .include(ResponseHeader.contentType("application/json"))
            }
    }

    private fun raiseAlarm(trackId: String): Completes<Response> {
        return Track.by(world, UUID.fromString(trackId))
            .andThenConsume { it.raiseAlarm() }
            .andThenTo { it.journey() }
            .andThen {
                Response.of(Response.Status.Ok, serialized(it))
                    .include(ResponseHeader.contentType("application/json"))
            }
    }

    private fun notifySafety(trackId: String): Completes<Response> {
        return Track.by(world, UUID.fromString(trackId))
            .andThenConsume { it.notifySafety() }
            .andThenTo { it.journey() }
            .andThen {
                Response.of(Response.Status.Ok, serialized(it))
                    .include(ResponseHeader.contentType("application/json"))
            }
    }

    private fun onError(t: Throwable): Completes<Response> {
        return Completes.withSuccess(Response.of(Response.Status.InternalServerError, serialized(t)))
    }
}