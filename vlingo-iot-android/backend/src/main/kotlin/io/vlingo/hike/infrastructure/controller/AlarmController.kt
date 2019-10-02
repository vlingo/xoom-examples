package io.vlingo.hike.infrastructure.controller

import io.vlingo.common.Completes
import io.vlingo.hike.domain.alarms.AlarmService
import io.vlingo.http.Response
import io.vlingo.http.ResponseHeader
import io.vlingo.http.resource.ResourceBuilder.get
import io.vlingo.http.resource.ResourceBuilder.resource
import io.vlingo.http.resource.serialization.JsonSerialization.serialized

class AlarmController(private val alarmService: AlarmService) {
    fun asResource(pool: Int) = resource("alarms", pool,
        get("/alarms")
            .handle(this::getAlarms)
            .onError(this::onError))

    private fun getAlarms(): Completes<Response> {
        return alarmService.allAlarms()
            .andThen {
                Response.of(Response.Status.Ok, serialized(it))
                    .include(ResponseHeader.contentType("application/json"))
            }

    }

    private fun onError(t: Throwable): Completes<Response> {
        return Completes.withSuccess(Response.of(Response.Status.InternalServerError, serialized(t)))
    }
}