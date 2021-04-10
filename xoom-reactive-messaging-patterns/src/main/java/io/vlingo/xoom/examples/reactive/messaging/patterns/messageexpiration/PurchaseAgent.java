// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.messageexpiration;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.DeadLetter;

/**
 * PurchaseAgent {@link Actor} responsible for placing an order for messages that have not yet expired.
 */
public class PurchaseAgent 
extends Actor 
implements OrderProcessor
{
    public final MessageExpirationResults results;
    
    public PurchaseAgent( final MessageExpirationResults results )
    {
        this.results = results;
    }
    
    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.messageexpiration.OrderProcessor#placeOrder(io.vlingo.xoom.examples.reactive.messaging.patterns.messageexpiration.Order) */
    @Override
    public void placeOrder( Order order )
    {
        if ( order.isExpired() )
        {
            this.deadLetters().failedDelivery( new DeadLetter( this, "stop()" ));
            results.access.writeUsing("afterOrderExpiredCount", 1);
            logger().debug( String.format( "PurchaseAgent: delivered expired %s to dead letters", order ));
        }
        else
        {
            results.access.writeUsing("afterOrderPlacedCount", 1);
            logger().debug( String.format( "PurchaseAgent: placing order for %s", order ));
        }

    }
}