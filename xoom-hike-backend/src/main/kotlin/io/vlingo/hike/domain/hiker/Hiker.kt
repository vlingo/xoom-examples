package io.vlingo.hike.domain.hiker

import io.vlingo.actors.Stage
import io.vlingo.hike.infrastructure.actors.actorFor
import java.util.*

interface Hiker {
    fun routeStarted()

    companion object {
        fun new(stage: Stage): Hiker = stage.actorFor<Hiker, HikerActor>(HikerId(UUID.randomUUID()))
    }
}