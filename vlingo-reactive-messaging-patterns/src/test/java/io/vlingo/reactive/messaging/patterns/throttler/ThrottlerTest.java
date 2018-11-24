package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.vlingo.actors.Definition.*;

public class ThrottlerTest {
    private World world;
    private Producer producer;
    private Consumer consumer;
    private TestUntil testUntil;

    @Before
    public void setUp() throws Exception {
        world = World.startWithDefaults("throttling");
        testUntil = TestUntil.happenings(10);

        Producer realTimeProducer = world.actorFor(has(RandomStringProducer.class, NoParameters), Producer.class);
        producer = world.actorFor(has(ThrottledProducer.class, parameters(1, 100, realTimeProducer)), Producer.class);
        consumer = world.actorFor(has(PrintingConsumer.class, parameters(testUntil)), Consumer.class);
    }

    @After
    public void tearDown() throws Exception {
        world.terminate();
    }

    @Test
    public void testThatThrottlesMessages() {
        long startTime = System.currentTimeMillis();
        sendNMessages(10);
        testUntil.completes();
        long elapsedTime = System.currentTimeMillis() - startTime;

        Assert.assertTrue("Processed messages in a faster rate", elapsedTime > 900);
        Assert.assertEquals(0, testUntil.remaining());
    }

    private void sendNMessages(int n) {
        for (int i = 0; i < n; i++) {
            producer.produceMessage(consumer);
        }
    }
}
