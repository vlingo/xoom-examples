// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.splitter;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem;

/**
 * OrderRouter receives an {@link Order} and routes each {@link OrderItem} instance according to its
 * {@link OrderItem#itemType}.
 */
public class OrderRouter 
extends Actor
implements OrderProcessor
{
    public final SplitterResults results;
    
    protected OrderItemProcessor orderItemTypeAProcessor;
    protected OrderItemProcessor orderItemTypeBProcessor;
    protected OrderItemProcessor orderItemTypeCProcessor;
    
    public OrderRouter( final SplitterResults results )
    {
        this.results = results;
    }
    
    /* @see io.vlingo.xoom.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        orderItemTypeAProcessor = childActorFor( OrderItemProcessor.class, Definition.has( OrderItemTypeAProcessor.class, Definition.parameters( results )) );
        orderItemTypeBProcessor = childActorFor( OrderItemProcessor.class, Definition.has( OrderItemTypeBProcessor.class, Definition.parameters( results )) );
        orderItemTypeCProcessor = childActorFor( OrderItemProcessor.class, Definition.has( OrderItemTypeCProcessor.class, Definition.parameters( results )) );
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderProcessor#placeOrder(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order) */
    @Override
    public void placeOrder( Order order )
    {
        for ( OrderItem item : order.orderItems.values() )
        {
            logger().debug( String.format( "OrderRouter: routing %s", item ));
            
            switch ( item.itemType )
            {
                case OrderProcessor.ITEM_TYPE_A:
                    orderItemTypeAProcessor.orderTypeAItem( item );
                    break;
                    
                case OrderProcessor.ITEM_TYPE_B:
                    orderItemTypeBProcessor.orderTypeBItem( item );
                    break;
                    
                case OrderProcessor.ITEM_TYPE_C:
                    orderItemTypeCProcessor.orderTypeCItem( item );
                    break;

                default:
                    logger().error( String.format( "Unknown item type '%s'", item.itemType ));
                    break;
            }
        }

        results.access.writeUsing("afterOrderPlacedCount", 1);
    }
    
    public static final class OrderItemTypeAProcessor
    extends Actor
    implements OrderItemProcessor
    {
        public final SplitterResults results;
        
        public OrderItemTypeAProcessor( final SplitterResults results )
        {
            this.results = results;
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeAItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeAItem(OrderItem orderItem)
        {
            logger().debug( String.format( "orderTypeAItem: handling %s", orderItem.toString() ));
            results.access.writeUsing("afterOrderByReceivedAProcessorCount", 1);
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeBItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeBItem(OrderItem orderItem)
        {
            logger().warn( "orderTypeBItem unsupported method" );
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeCItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeCItem(OrderItem orderItem)
        {
            logger().warn( "orderTypeCItem unsupported method" );
        }
        
    }
    
    public static final class OrderItemTypeBProcessor
    extends Actor
    implements OrderItemProcessor
    {
        public final SplitterResults results;
        
        public OrderItemTypeBProcessor( final SplitterResults results )
        {
            this.results = results;
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeAItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeAItem(OrderItem orderItem)
        {
            logger().warn( "orderTypeAItem unsupported method" );
       }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeBItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeBItem(OrderItem orderItem)
        {
            logger().debug( String.format( "orderTypeBItem: handling %s", orderItem.toString() ));
            results.access.writeUsing("afterOrderByReceivedBProcessorCount", 1);
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeCItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeCItem(OrderItem orderItem)
        {
            logger().warn( "orderTypeCItem unsupported method" );
        }
        
    }
    
    public static final class OrderItemTypeCProcessor
    extends Actor
    implements OrderItemProcessor
    {
        public final SplitterResults results;
        
        public OrderItemTypeCProcessor( final SplitterResults results )
        {
            this.results = results;
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeAItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeAItem(OrderItem orderItem)
        {
            logger().warn( "orderTypeAItem unsupported method" );
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeBItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeBItem(OrderItem orderItem)
        {
            logger().warn( "orderTypeBItem unsupported method" );
        }

        /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.OrderRouter.OrderItemProcessor#orderTypeCItem(io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem) */
        @Override
        public void orderTypeCItem(OrderItem orderItem)
        {
            logger().debug( String.format( "orderTypeCItem: handling %s", orderItem.toString() ));
            results.access.writeUsing("afterOrderByReceivedCProcessorCount", 1);
        }
        
    }

}
