package io.vlingo

import io.vlingo.actors.Stoppable

interface Pinger: Stoppable {

    fun ping(ponger:Ponger)

}