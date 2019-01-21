package io.vlingo.hike.infrastructure.actors

import io.vlingo.actors.Actor
import io.vlingo.actors.Stage

inline fun <reified P, reified I> Stage.actorFor(vararg params: Any): P where I: P {
    return this.actorFor(P::class.java, I::class.java as Class<out Actor>, *params)
}