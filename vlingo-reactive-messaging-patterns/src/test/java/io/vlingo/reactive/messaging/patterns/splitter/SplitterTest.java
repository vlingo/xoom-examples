// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.splitter;

import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem;;

/**
 * SplitterTest demonstrates a composite message being split into its individual parts and sent as 
 * smaller messages.
 *
 * @author brsg.io
 * @since Nov 5, 2018
 */
public class SplitterTest
{
    public static final String WORLD_NAME = "splitter-example";
    public static final int ORDERS_ITEMS = 4;

    @Test
    public void testSplitterRuns()
    {
        World world = World.startWithDefaults( WORLD_NAME );
        world.defaultLogger().log( "SplitterTest: is started" );
        
        TestUntil until = TestUntil.happenings( ORDERS_ITEMS );
        
        final OrderProcessor orderRouter = world.actorFor( Definition.has( OrderRouter.class, Definition.parameters( until )), OrderProcessor.class );
        
        final OrderItem orderItem1 = new OrderItem( "1", OrderProcessor.ITEM_TYPE_A, "An item of type A", 23.95 );
        final OrderItem orderItem2 = new OrderItem( "2", OrderProcessor.ITEM_TYPE_B, "An item of type B", 99.95 );
        final OrderItem orderItem3 = new OrderItem( "3", OrderProcessor.ITEM_TYPE_C, "An item of type C", 14.95 );
        final OrderItem[] orderItems = { orderItem1, orderItem2, orderItem3 };
        final Order order = new Order( orderItems );
        
        orderRouter.placeOrder( order );
        
        until.completes();

        world.defaultLogger().log( "SplitterTest: is completed" );
        world.terminate();
    }

}
