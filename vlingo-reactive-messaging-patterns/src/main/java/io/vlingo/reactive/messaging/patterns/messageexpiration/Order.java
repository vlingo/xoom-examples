// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messageexpiration;

/**
 * Order message that carries the criteria for an order as well as its expiration.
 */
public class Order
implements ExpiringMessage
{
    public String id;
    public String itemId;
    public Double price;
    public Long timeToLive;
    public Long occuredOn;
    
    public Order( String anId, String anItemId, Double aPrice, Long ttl )
    {
        this.id = anId;
        this.itemId = anItemId;
        this.price = aPrice;
        this.timeToLive = ttl;
        this.occuredOn = System.currentTimeMillis();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messageexpiration.ExpiringMessage#isExpired() */
    @Override
    public Boolean isExpired()
    {
        Long elapsed = System.currentTimeMillis() - occuredOn;
        return elapsed > timeToLive;
    }

    /* @see java.lang.Object#toString() */
    @Override
    public String toString()
    {
        return String.format( "Order( %s, %s, %.2f )", id, itemId, price );
    }
}
