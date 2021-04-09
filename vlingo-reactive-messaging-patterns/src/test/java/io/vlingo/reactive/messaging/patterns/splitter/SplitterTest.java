// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.splitter;

import org.junit.Assert;
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem;;

/**
 * SplitterTest demonstrates a composite message being split into its individual parts and sent as 
 * smaller messages.
 */
public class SplitterTest
{
    public static final String WORLD_NAME = "splitter-example";
    public static final int ORDERS_ITEMS = 4;

    @Test
    public void testSplitterRuns()
    {
        World world = World.startWithDefaults( WORLD_NAME );
        world.defaultLogger().debug( "SplitterTest: is started" );
        
        final SplitterResults results = new SplitterResults();
        final AccessSafely access = results.afterCompleting( ORDERS_ITEMS );
        
        final OrderProcessor orderRouter = world.actorFor( OrderProcessor.class, OrderRouter.class, results );
        
        final OrderItem orderItem1 = new OrderItem( "1", OrderProcessor.ITEM_TYPE_A, "An item of type A", 23.95 );
        final OrderItem orderItem2 = new OrderItem( "2", OrderProcessor.ITEM_TYPE_B, "An item of type B", 99.95 );
        final OrderItem orderItem3 = new OrderItem( "3", OrderProcessor.ITEM_TYPE_C, "An item of type C", 14.95 );
        final OrderItem[] orderItems = { orderItem1, orderItem2, orderItem3 };
        final Order order = new Order( orderItems );
        
        orderRouter.placeOrder( order );

        Assert.assertEquals(1, (int) access.readFrom("afterOrderPlacedCount"));
        Assert.assertEquals(1, (int) access.readFrom("afterOrderByReceivedAProcessorCount"));
        Assert.assertEquals(1, (int) access.readFrom("afterOrderByReceivedBProcessorCount"));
        Assert.assertEquals(1, (int) access.readFrom("afterOrderByReceivedCProcessorCount"));

        world.defaultLogger().debug( "SplitterTest: is completed" );
        world.terminate();
    }

}
