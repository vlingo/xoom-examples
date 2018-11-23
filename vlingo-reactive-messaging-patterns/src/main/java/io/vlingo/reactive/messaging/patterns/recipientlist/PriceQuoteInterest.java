// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

/**
 * PriceQuoteInterest used to register interest in providing {@link PriceQuote} for each retail item where appropriate.
 *
 * @author brsg.io
 * @since Nov 20, 2018
 */
public class PriceQuoteInterest
{
    public final String path;
    public final QuoteProcessor quoteProcessor;
    public final Double lowTotalRetail;
    public final Double highTotalRetail;
    
    public PriceQuoteInterest( final String aPath, final QuoteProcessor aProcessor, final Double lowTotal, final Double highTotal )
    {
        this.path = aPath;
        this.quoteProcessor = aProcessor;
        this.lowTotalRetail = lowTotal;
        this.highTotalRetail = highTotal;
    }
    
    public static class PriceQuote
    {
        public final QuoteProcessor quoteProcessor;
        public final String rfqId;
        public final String itemId;
        public final Double retailPrice;
        public final Double discountPrice;
        
        public PriceQuote( final QuoteProcessor qProcessor, final String anRfqId, final String anItemId, final Double aRetailPrice, final Double aDiscountPrice )
        {
            this.quoteProcessor = qProcessor;
            this.rfqId = anRfqId;
            this.itemId = anItemId;
            this.retailPrice = aRetailPrice;
            this.discountPrice = aDiscountPrice;
        }

        /* @see java.lang.Object#toString() */
        @Override
        public String toString()
        {
            return String.format( "%s %s %s $%.2f $%.2f", quoteProcessor.getClass().getSimpleName(), rfqId, itemId, retailPrice, discountPrice );
        }
    }
}
