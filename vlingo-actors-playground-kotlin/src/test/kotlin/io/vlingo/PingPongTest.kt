package io.vlingo

import io.vlingo.actors.Definition
import io.vlingo.actors.World
import io.vlingo.actors.testkit.TestUntil
import org.junit.Test

class PingPongTest {

    @Test
    fun testPlayPingPong() {
        val world:World = World.startWithDefaults("com.dfp")
        val until:TestUntil = TestUntil.happenings(1)
        val pinger:Pinger = world.actorFor(Pinger::class.java, PingerActor::class.java, until)
        val ponger:Ponger = world.actorFor(Ponger::class.java, PongerActor::class.java)

        pinger.ping(ponger)

        until.completes()

        world.terminate()
    }
}