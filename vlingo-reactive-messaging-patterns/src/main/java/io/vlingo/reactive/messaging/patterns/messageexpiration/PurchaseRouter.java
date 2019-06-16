// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messageexpiration;

import io.vlingo.actors.Actor;
import io.vlingo.common.Scheduled;

import java.util.Random;

/**
 * PurchaseRouter {@link Actor} responsible for randomly delaying the delivery of an order message such
 * that message expiration can be demonstrated. 
 */
public class PurchaseRouter 
extends Actor 
implements OrderProcessor
{
    public final OrderProcessor purchaseAgent;
    
    public PurchaseRouter( OrderProcessor agent )
    {
        this.purchaseAgent = agent;
    }

    /* @see io.vlingo.reactive.messaging.patterns.messageexpiration.OrderProcessor#placeOrder(io.vlingo.reactive.messaging.patterns.messageexpiration.Order) */
    @Override
    public void placeOrder(Order order)
    {
        Random random = new Random();
        int millis = random.nextInt( 100 ) + 1;
        logger().debug( String.format( "PurchaseRouter: delaying delivery of %s for %d milliseconds", order, millis ) );
        Scheduled<Order> scheduled = 
            new Scheduled<Order>()
            {
                @Override
                public void intervalSignal(Scheduled<Order> scheduled, Order data)
                {
                    purchaseAgent.placeOrder(data);
                }
            };
        this.scheduler().scheduleOnce( scheduled, order, millis, 0L );
    }

}