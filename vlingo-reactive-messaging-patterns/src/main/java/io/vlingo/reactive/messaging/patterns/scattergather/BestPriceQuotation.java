// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import java.util.Vector;

/**
 * BestPriceQuotation represents data objects in support of placing, aggregating, and fulfilling price quotes.
 */
public class BestPriceQuotation
{
    public final String rfqId;
    public final Vector<PriceQuote> priceQuotes;
    
    public BestPriceQuotation( String rfqId, PriceQuote...quotes )
    {
        this.rfqId = rfqId;
        this.priceQuotes = new Vector<>();
        for ( PriceQuote quote : quotes )
        {
            this.priceQuotes.add( quote );
        }
    }

    /* @see java.lang.Object#toString() */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( String.format( "%s: ", rfqId ));
        Object[] quotes = null;
        if ( priceQuotes.size() > 0 )
        {
            for ( int i = 0; i < priceQuotes.size(); i++ )
            {
                sb.append( " (%s) " );
            }
            quotes = priceQuotes.toArray( new PriceQuote[ priceQuotes.size() ]);
        }
        return priceQuotes.size() > 0 ? String.format( sb.toString(), quotes ) : sb.toString();
    }
    
    public static class PriceQuote
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
    
    public static class QuotationFulfillment
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
    
    public static class PriceQuoteTimedOut
    {
        public final String rfqId;
        
        public PriceQuoteTimedOut( String rfqId )
        {
            this.rfqId = rfqId;
        }
    }
}
