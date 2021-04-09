package io.vlingo.xoom.examples.pingpong

import io.vlingo.xoom.actors.Stoppable

interface Ponger: Stoppable {

    fun pong(pinger: Pinger)
}