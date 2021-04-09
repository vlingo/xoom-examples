package io.vlingo.xoom.examples.pingpong

import io.vlingo.xoom.actors.Actor
import io.vlingo.xoom.actors.testkit.TestUntil

class PingerActor: Actor, Pinger {

    var count:Int
    val pinger:Pinger
    val until:TestUntil

    constructor(until: TestUntil) {
        this.until = until
        this.count = 0
        this.pinger = selfAs(Pinger::class.java)
    }


    override fun ping(ponger: Ponger) {
        ++count
        logger().debug("ping $count")
        if (count > 10) {
            pinger.stop()
            ponger.stop()
        } else {
            ponger.pong(pinger)
        }
    }

    override fun afterStop() {
        logger().debug("Pinger: ${address()} just stopped")
        until.happened()
        super.afterStop()
    }
}