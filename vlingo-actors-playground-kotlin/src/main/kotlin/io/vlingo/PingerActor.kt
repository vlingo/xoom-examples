package io.vlingo

import io.vlingo.actors.Actor
import io.vlingo.actors.testkit.TestUntil

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
        logger().log("ping $count")
        if (count > 10) {
            pinger.stop()
            ponger.stop()
        } else {
            ponger.pong(pinger)
        }
    }

    override fun afterStop() {
        logger().log("Pinger: ${address()} just stopped")
        until.happened()
        super.afterStop()
    }
}