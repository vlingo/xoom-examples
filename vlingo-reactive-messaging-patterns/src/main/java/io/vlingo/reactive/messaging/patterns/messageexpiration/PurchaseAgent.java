// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messageexpiration;

import io.vlingo.actors.Actor;
import io.vlingo.actors.DeadLetter;
import io.vlingo.actors.testkit.TestUntil;

/**
 * PurchaseAgent {@link Actor} responsible for placing an order for messages that have not yet expired.
 */
public class PurchaseAgent 
extends Actor 
implements OrderProcessor
{
    public final TestUntil until;
    
    public PurchaseAgent( TestUntil until )
    {
        this.until = until;
    }
    
    /* @see io.vlingo.reactive.messaging.patterns.messageexpiration.OrderProcessor#placeOrder(io.vlingo.reactive.messaging.patterns.messageexpiration.Order) */
    @Override
    public void placeOrder( Order order )
    {
        if ( order.isExpired() )
        {
            this.deadLetters().failedDelivery( new DeadLetter( this, "stop()" ));
            logger().log( String.format( "PurchaseAgent: delivered expired %s to dead letters", order ));
        }
        else
        {
            logger().log( String.format( "PurchaseAgent: placing order for %s", order ));
        }
        until.happened();
    }
}