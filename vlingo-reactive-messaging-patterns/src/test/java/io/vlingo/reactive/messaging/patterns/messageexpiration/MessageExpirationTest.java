// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messageexpiration;

import io.vlingo.actors.testkit.AccessSafely;
import org.junit.Assert;
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

/**
 * MessageExpirationTest
 */
public class MessageExpirationTest
{
    public static final String WORLD_NAME = "message-expiration-example";
    public static final int ORDERS = 3;

    @Test
    public void testMessageExpirationRuns()
    {
        World world = World.startWithDefaults( WORLD_NAME );
        world.defaultLogger().log( "MessageExpirationTest: is started" );

        final MessageExpirationResults results = new MessageExpirationResults();
        final AccessSafely access = results.afterCompleting( ORDERS );
        
        OrderProcessor purchaseAgent = world.actorFor(OrderProcessor.class, PurchaseAgent.class, results);
        OrderProcessor purchaseRouter = world.actorFor(OrderProcessor.class, PurchaseRouter.class, purchaseAgent);
        
        purchaseRouter.placeOrder( new Order( "1", "11", 50.00, 1000L ));
        purchaseRouter.placeOrder( new Order( "2", "22", 250.00, 100L ));
        purchaseRouter.placeOrder( new Order( "3", "33", 32.95, 10L ));

        Assert.assertEquals(2, (int) access.readFrom("afterOrderPlacedCount"));
        Assert.assertEquals(1, (int) access.readFrom("afterOrderExpiredCount"));
        
        world.defaultLogger().log( "MessageExpirationTest: is completed" );
        
        world.terminate();
    }

}
