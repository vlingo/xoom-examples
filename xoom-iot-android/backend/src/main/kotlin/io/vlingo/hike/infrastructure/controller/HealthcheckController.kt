package io.vlingo.hike.infrastructure.controller

import io.vlingo.common.Completes
import io.vlingo.http.Response
import io.vlingo.http.resource.ResourceBuilder.get
import io.vlingo.http.resource.ResourceBuilder.resource
import io.vlingo.http.resource.serialization.JsonSerialization.serialized


object HealthcheckController {
    fun asResource(pool: Int) = resource(
        "healthcheck", pool,
        get("/healthcheck")
            .handle(this::ok)
            .onError(this::ko),
        get("/")
            .handle(this::ok)
            .onError(this::ko)
    )

    private fun ok(): Completes<Response> {
        return Completes.withSuccess(Response.of(Response.Status.Ok, serialized("Ok")))

    }

    private fun ko(t: Throwable): Completes<Response> {
        return Completes.withSuccess(Response.of(Response.Status.InternalServerError, serialized(t)))
    }
}