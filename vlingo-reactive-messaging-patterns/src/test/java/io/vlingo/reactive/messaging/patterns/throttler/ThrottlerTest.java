// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.throttler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

public class ThrottlerTest {
    private World world;
    private Producer producer;
    private Consumer consumer;
    private TestUntil testUntil;

    @Before
    public void setUp() throws Exception {
        world = World.startWithDefaults("throttling");
        testUntil = TestUntil.happenings(10);

        Producer realTimeProducer = world.actorFor(Producer.class, RandomStringProducer.class );
        producer = world.actorFor(Producer.class, ThrottledProducer.class, 1, 100, realTimeProducer );
        consumer = world.actorFor(Consumer.class, PrintingConsumer.class, testUntil );
    }

    @After
    public void tearDown() throws Exception {
        world.terminate();
    }

    @Test
    public void testThatThrottlesMessages() {
        // TODO: note that we can't reliably use tests that assert specific timings

        //long startTime = System.currentTimeMillis();
        sendNMessages(10);
        testUntil.completes();
        //long elapsedTime = System.currentTimeMillis() - startTime;

        //Assert.assertTrue("Processed messages in a faster rate (10 messages in " + elapsedTime + "ms)", elapsedTime >= 850);
        Assert.assertEquals(0, testUntil.remaining());
    }

    private void sendNMessages(int n) {
        for (int i = 0; i < n; i++) {
            producer.produceMessage(consumer);
        }
    }
}
