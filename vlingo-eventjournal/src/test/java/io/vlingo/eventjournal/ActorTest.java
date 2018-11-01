package io.vlingo.eventjournal;

import io.vlingo.actors.World;
import org.junit.After;
import org.junit.Before;

public abstract class ActorTest {
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
