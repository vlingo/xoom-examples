package io.vlingo.hike.domain.hiker

import io.vlingo.hike.domain.hiker.events.RouteStarted
import io.vlingo.hike.infrastructure.actors.eventConsumer
import io.vlingo.lattice.model.sourcing.EventSourced
import java.time.LocalDateTime

class HikerActor(
        private val id: HikerId
): EventSourced<String>(), Hiker {
    private var currentRoute: Route? = null

    override fun streamName(): String =
            streamNameFrom("_", "hiker", id.uuid.toString())

    override fun routeStarted() {
        if (currentRoute === null) {
            apply(RouteStarted(LocalDateTime.now()))
        }
    }

    companion object {
        init {
            eventConsumer(HikerActor::whenRouteStarted)
        }
    }

    private fun whenRouteStarted(routeStarted: RouteStarted) {
        println("FOOOOO")
    }
}