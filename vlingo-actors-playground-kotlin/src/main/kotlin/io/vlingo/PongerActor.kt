package io.vlingo

import io.vlingo.actors.Actor

class PongerActor: Actor, Ponger {

    val ponger:Ponger

    constructor() {
        this.ponger = selfAs(Ponger::class.java)
    }

    override fun pong(pinger: Pinger) {
        pinger.ping(ponger)
    }
}