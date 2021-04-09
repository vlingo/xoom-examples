// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.splitter;

import java.util.HashMap;
import java.util.Map;

/**
 * Order is composed of {@link OrderItem} instances.
 */
public class Order 
{
    public final Map<String, OrderItem> orderItems;
    public final Double grandTotal;
    
    public Order( final OrderItem...items )
    {
        orderItems = new HashMap<>();
        for ( OrderItem item : items )
        {
            orderItems.put( item.itemType, item );
        }
        grandTotal = orderItems.entrySet().stream().mapToDouble( value -> value.getValue().price ).sum();
    }
    
    /**
     * OrderItem atomic elements to be processed individually.
     */
    public static final class OrderItem 
    {
        public final String id;
        public final String itemType;
        public final String description;
        public final Double price;
        
        public OrderItem( final String id, final String itemType, final String description, final Double price )
        {
            this.id = id;
            this.itemType = itemType;
            this.description = description;
            this.price = price;
        }

        /* @see java.lang.Object#toString() */
        @Override
        public String toString()
        {
            return String.format( "OrderItem %s %s %s %.2f", id, itemType, description, price );
        }
        
    }
}
