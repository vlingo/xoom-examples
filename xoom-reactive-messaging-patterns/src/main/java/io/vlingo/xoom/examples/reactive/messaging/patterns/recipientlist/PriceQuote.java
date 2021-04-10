// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.recipientlist;

/**
 * PriceQuote price quote being requested.
 */
public class PriceQuote
{
    public final QuoteProcessor quoteProcessor;
    public final String rfqId;
    public final String itemId;
    public final Double retailPrice;
    public final Double discountPrice;
    
    public PriceQuote( final QuoteProcessor quoteProcessor, final String rfqId, final String itemId, final Double retailPrice, final Double discountPrice )
    {
        this.quoteProcessor = quoteProcessor;
        this.rfqId = rfqId;
        this.itemId = itemId;
        this.retailPrice = retailPrice;
        this.discountPrice = discountPrice;
    }

    /* @see java.lang.Object#toString() */
    @Override
    public String toString()
    {
        return String.format( "%s %s %s $%.2f $%.2f", quoteProcessor.getClass().getSimpleName(), rfqId, itemId, retailPrice, discountPrice );
    }
}
