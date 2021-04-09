// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

/**
 * RetailItem id of item to be purchased and its price.
 */
public class RetailItem
{
    public final String itemId;
    public final Double retailPrice;
    
    public RetailItem( final String itemId, final Double retailPrice )
    {
        this.itemId = itemId;
        this.retailPrice = retailPrice;
    }
}
