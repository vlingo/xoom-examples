package com.example.vlingohike

import io.vlingo.actors.testkit.TestWorld
import org.junit.After
import org.junit.Before

abstract class ActorTest {
    private lateinit var testWorld: TestWorld

    @Before
    fun setUpWorld() {
        testWorld = TestWorld.startWithDefaults(javaClass.simpleName)
    }

    @After
    fun tearDownWorld() {
        testWorld.terminate()
    }

    protected fun world() = testWorld.world()
}