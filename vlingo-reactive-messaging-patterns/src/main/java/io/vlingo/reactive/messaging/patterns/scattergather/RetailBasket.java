// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import java.util.Vector;

/**
 * RetailBasket basket of {@link RetailItem} to be quoted for discount where appropriate.
 */
public class RetailBasket
{
    public final String rfqId;
    public final Vector<RetailItem> retailItems;
    public final Double totalRetailPrice;
    
    public RetailBasket( final String rfqId, final RetailItem...items )
    {
        this.rfqId = rfqId;
        this.retailItems = new Vector<>();
        double d = 0;
        for ( RetailItem item : items ) 
        { 
            retailItems.add( item );
            d += item.retailPrice; 
        }
        totalRetailPrice = d;
    }
}
