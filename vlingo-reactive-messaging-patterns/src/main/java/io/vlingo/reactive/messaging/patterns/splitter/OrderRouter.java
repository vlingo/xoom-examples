// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.splitter;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem;

/**
 * OrderRouter receives an {@link Order} and routes each {@link OrderItem} instance according to its
 * {@link OrderItem#itemType}.
 *
 * @author brsg.io
 * @since Nov 5, 2018
 */
public class OrderRouter 
extends Actor
implements OrderProcessor
{
    public final TestUntil until;
    
    protected OrderItemProcessor orderItemTypeAProcessor;
    protected OrderItemProcessor orderItemTypeBProcessor;
    protected OrderItemProcessor orderItemTypeCProcessor;
    
    public OrderRouter( TestUntil until )
    {
        this.until = until;
    }
    
    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        orderItemTypeAProcessor = childActorFor( Definition.has( OrderItemTypeAProcessor.class, Definition.parameters( until )), OrderItemProcessor.class );
        orderItemTypeBProcessor = childActorFor( Definition.has( OrderItemTypeBProcessor.class, Definition.parameters( until ) ), OrderItemProcessor.class );
        orderItemTypeCProcessor = childActorFor( Definition.has( OrderItemTypeCProcessor.class, Definition.parameters( until ) ), OrderItemProcessor.class );
    }

    /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderProcessor#placeOrder(io.vlingo.reactive.messaging.patterns.splitter.Order) */
    @Override
    public void placeOrder( Order order )
    {
        for ( OrderItem item : order.orderItems.values() )
        {
            logger().log( String.format( "OrderRouter: routing %s", item ));
            if ( OrderProcessor.ITEM_TYPE_A.equals( item.itemType ))
            {
                orderItemTypeAProcessor.orderTypeAItem( item );
            }
            else if ( OrderProcessor.ITEM_TYPE_B.equals( item.itemType ))
            {
                orderItemTypeBProcessor.orderTypeBItem( item );
            }
            else if ( OrderProcessor.ITEM_TYPE_C.equals( item.itemType ))
            {
                orderItemTypeCProcessor.orderTypeCItem( item );
            }
        }
        
        until.happened();
    }
    
    public static final class OrderItemTypeAProcessor
    extends Actor
    implements OrderItemProcessor
    {
        public final TestUntil until;
        
        public OrderItemTypeAProcessor( TestUntil until )
        {
            this.until = until;
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeAItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeAItem(OrderItem orderItem)
        {
            logger().log( String.format( "orderTypeAItem: handling %s", orderItem.toString() ));
            until.happened();
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeBItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeBItem(OrderItem orderItem)
        {
            logger().log( "orderTypeBItem unsupported method" );
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeCItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeCItem(OrderItem orderItem)
        {
            logger().log( "orderTypeCItem unsupported method" );
        }
        
    }
    
    public static final class OrderItemTypeBProcessor
    extends Actor
    implements OrderItemProcessor
    {
        public final TestUntil until;
        
        public OrderItemTypeBProcessor( TestUntil until )
        {
            this.until = until;
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeAItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeAItem(OrderItem orderItem)
        {
            logger().log( "orderTypeAItem unsupported method" );
       }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeBItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeBItem(OrderItem orderItem)
        {
            logger().log( String.format( "orderTypeBItem: handling %s", orderItem.toString() ));
            until.happened();
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeCItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeCItem(OrderItem orderItem)
        {
            logger().log( "orderTypeCItem unsupported method" );
        }
        
    }
    
    public static final class OrderItemTypeCProcessor
    extends Actor
    implements OrderItemProcessor
    {
        public final TestUntil until;
        
        public OrderItemTypeCProcessor( TestUntil until )
        {
            this.until = until;
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeAItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeAItem(OrderItem orderItem)
        {
            logger().log( "orderTypeAItem unsupported method" );
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeBItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeBItem(OrderItem orderItem)
        {
            logger().log( "orderTypeBItem unsupported method" );
        }

        /* @see io.vlingo.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeCItem(io.vlingo.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeCItem(OrderItem orderItem)
        {
            logger().log( String.format( "orderTypeCItem: handling %s", orderItem.toString() ));
            until.happened();
        }
        
    }

}
