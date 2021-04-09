package io.vlingo.xoom.examples.pingpong

import io.vlingo.xoom.actors.Stoppable

interface Pinger: Stoppable {

    fun ping(ponger:Ponger)

}