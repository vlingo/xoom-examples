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
        val pinger:Pinger = world.actorFor(Definition.has(PingerActor::class.java, Definition.parameters(until)), Pinger::class.java)
        val ponger:Ponger = world.actorFor(Definition.has(PongerActor::class.java, Definition.NoParameters), Ponger::class.java)

        pinger.ping(ponger)

        until.completes()

        world.terminate()

    }
}