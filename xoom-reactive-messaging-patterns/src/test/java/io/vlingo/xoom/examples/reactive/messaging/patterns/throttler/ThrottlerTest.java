// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.throttler;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ThrottlerTest {
    private World world;
    private Producer producer;
    private Consumer consumer;
    private ThrottlerResults results;
    private AccessSafely accessSafely;

    private static final int NUMBER_OF_MESSAGES = 10;

    @Before
    public void setUp() {
        world = World.startWithDefaults("throttling");
        results = new ThrottlerResults();
        accessSafely = results.afterCompleting(NUMBER_OF_MESSAGES);

        Producer realTimeProducer = world.actorFor(Producer.class, RandomStringProducer.class );
        producer = world.actorFor(Producer.class, ThrottledProducer.class, 1, 100, realTimeProducer );
        consumer = world.actorFor(Consumer.class, PrintingConsumer.class, results );
    }

    @After
    public void tearDown() throws Exception {
        world.terminate();
    }

    @Test
    public void testThatThrottlesMessages() {
        // TODO: note that we can't reliably use tests that assert specific timings

        //long startTime = System.currentTimeMillis();
        sendNMessages(NUMBER_OF_MESSAGES);
        //long elapsedTime = System.currentTimeMillis() - startTime;

        //Assert.assertTrue("Processed messages in a faster rate (10 messages in " + elapsedTime + "ms)", elapsedTime >= 850);
        Assert.assertEquals(NUMBER_OF_MESSAGES, (int) accessSafely.readFrom("afterMessageReceivedCount"));
    }

    private void sendNMessages(int n) {
        for (int i = 0; i < n; i++) {
            producer.produceMessage(consumer);
        }
    }
}
