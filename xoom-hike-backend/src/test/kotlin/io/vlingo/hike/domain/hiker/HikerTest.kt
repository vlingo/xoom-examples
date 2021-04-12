package io.vlingo.hike.domain.hiker

import io.vlingo.hike.ActorTest
import io.vlingo.hike.domain.hiker.events.RouteStarted
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HikerTest: ActorTest() {
    private lateinit var hiker: Hiker

    @Before
    fun setUp() {
        hiker = Hiker.new(stage())
    }

    @Test
    fun testThatCreatesANewRoute() {
        val events = withExpectedEvents(1) {
            hiker.routeStarted()
        }

        assertEquals(RouteStarted::class.java.canonicalName, events[0].type)
    }
}