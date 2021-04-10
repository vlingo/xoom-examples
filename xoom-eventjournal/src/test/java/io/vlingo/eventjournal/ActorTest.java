package io.vlingo.xoom.examples.eventjournal;

import org.junit.After;
import org.junit.Before;

import io.vlingo.xoom.actors.World;

public abstract class ActorTest {
    protected static final int TIMEOUT = 1000;
    private World world;

    @Before
    public void setUpWorld() throws Exception {
        world = World.startWithDefaults("test");
    }

    @After
    public void tearDownWorld() throws Exception {
        world.terminate();
    }

    protected final World world() {
        return world;
    }
}
