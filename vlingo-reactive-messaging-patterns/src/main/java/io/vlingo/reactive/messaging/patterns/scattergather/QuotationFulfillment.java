// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import java.util.Vector;

/**
 * QuotationFulfillment
 */
public class QuotationFulfillment
{
    public final String rfqId;
    public final Integer quotesRequested;
    public final Vector<PriceQuote> priceQuotes;
    public final OrderProcessor requestor;
    
    public QuotationFulfillment( final String rfqId, Integer quotesRequested, OrderProcessor requestor, PriceQuote...quotes )
    {
        this.rfqId = rfqId;
        this.quotesRequested = quotesRequested;
        this.requestor = requestor;
        this.priceQuotes = new Vector<>();
        for ( PriceQuote quote : quotes )
        {
            priceQuotes.add( quote );
        }
    }
}
