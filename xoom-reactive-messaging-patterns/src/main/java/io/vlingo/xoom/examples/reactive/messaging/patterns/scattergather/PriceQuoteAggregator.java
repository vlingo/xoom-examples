// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.common.Scheduled;

/**
 * PriceQuoteAggregator {@link Actor} responsible for tracking, expiring and fulfilling price quotes.
 */
public class PriceQuoteAggregator 
extends Actor 
implements AggregateProcessor
{
    public final Map<String, QuotationFulfillment> fulfilledPriceQuotes;
    
    public PriceQuoteAggregator()
    {
        this.fulfilledPriceQuotes = new HashMap<>();
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather.AggregateProcessor#requiredPriceQuoteForFulfillment(java.lang.String, java.lang.Integer, io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather.OrderProcessor) */
    @Override
    public void requiredPriceQuoteForFulfillment( String rfqId, Integer quotesRequested, OrderProcessor orderProcessor )
    {
        QuotationFulfillment quotationFulfillment = new QuotationFulfillment( rfqId, quotesRequested, orderProcessor );
        fulfilledPriceQuotes.put( rfqId, quotationFulfillment );
        Scheduled<String> scheduled = new Scheduled<String>()
        {
            @Override
            public void intervalSignal(Scheduled<String> scheduled, String rfqId)
            {
                priceQuoteTimedOut( new PriceQuoteTimedOut( rfqId ));
            }
        };
        this.scheduler().scheduleOnce( scheduled, rfqId, 2000L, 0L );
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather.AggregateProcessor#priceQuoteFulfilled(io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather.PriceQuoteInterest.PriceQuote) */
    @Override
    public void priceQuoteFulfilled( PriceQuote priceQuote )
    {
        priceQuoteRequestFulfilled( priceQuote );
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather.AggregateProcessor#priceQuoteTimedOut(io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather.PriceQuoteTimedOut) */
    @Override
    public void priceQuoteTimedOut( PriceQuoteTimedOut priceQuoteTimedOut )
    {
        priceQuoteRequestTimedOut( priceQuoteTimedOut.rfqId );
    }

    protected void priceQuoteRequestFulfilled(PriceQuote priceQuote )
    {
        String rfqId = priceQuote.rfqId;
        if ( fulfilledPriceQuotes.containsKey( rfqId ))
        {
            QuotationFulfillment previousFulfillment = fulfilledPriceQuotes.get( rfqId );
            Vector<PriceQuote> vQuotes = previousFulfillment.priceQuotes;
            PriceQuote[] priceQuotes = vQuotes.toArray( new PriceQuote[ vQuotes.size() + 1 ]);
            priceQuotes[ vQuotes.size() ] = priceQuote;
            QuotationFulfillment currentFulfillment = 
                new QuotationFulfillment(
                    rfqId, 
                    previousFulfillment.quotesRequested, 
                    previousFulfillment.requestor, 
                    priceQuotes 
                );
            if ( priceQuotes.length >= currentFulfillment.quotesRequested )
            {
                quoteBestPrice( currentFulfillment );
            }
            else
            {
                fulfilledPriceQuotes.put( rfqId, currentFulfillment );
            }
        }
    }

    protected void priceQuoteRequestTimedOut( String rfqId )
    {
        if ( fulfilledPriceQuotes.containsKey( rfqId ))
        {
            quoteBestPrice( fulfilledPriceQuotes.get( rfqId ));
        }
    }

    protected void quoteBestPrice( QuotationFulfillment quotationFulfillment )
    {
        String rfqId = quotationFulfillment.rfqId;
        if ( fulfilledPriceQuotes.containsKey( rfqId ))
        {
            quotationFulfillment.requestor.bestPriceQuotation( bestPriceQuotationFrom( quotationFulfillment ));
            fulfilledPriceQuotes.remove( rfqId );
        }
    }
    
    protected BestPriceQuotation bestPriceQuotationFrom( QuotationFulfillment quotationFulfillment )
    {
        Map<String, PriceQuote> bestPrices = new HashMap<>();
        for ( PriceQuote quote : quotationFulfillment.priceQuotes )
        {
            if ( bestPrices.containsKey( quote.itemId ))
            {
                if ( bestPrices.get( quote.itemId ).discountPrice > quote.discountPrice )
                {
                    bestPrices.put( quote.itemId, quote );
                }
            }
            else
            {
                bestPrices.put( quote.itemId, quote );
            }
        }
        PriceQuote[] priceQuotes = bestPrices.values().toArray( new PriceQuote[ bestPrices.size() ]);
        return new BestPriceQuotation( quotationFulfillment.rfqId, priceQuotes );
    }
}
