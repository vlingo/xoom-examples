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
}
