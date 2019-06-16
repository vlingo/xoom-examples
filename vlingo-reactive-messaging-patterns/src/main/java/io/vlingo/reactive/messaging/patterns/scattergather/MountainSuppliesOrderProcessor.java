// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import io.vlingo.actors.Actor;

import java.util.HashMap;
import java.util.Map;

/**
 * MountainSuppliesOrderProcessor maintains registry of @{@link QuoteProcessor} {@link Actor} instances
 * interested in providing quotes according to constraints on total retail price of a basket of items. 
 */
public class MountainSuppliesOrderProcessor 
extends Actor 
implements OrderProcessor
{
    public final AggregateProcessor priceQuoteAggregator;
    public final ScatterGatherResults results;
    public final Map<String, QuoteSubscriptionRequest> subscribers;
    
    public MountainSuppliesOrderProcessor( AggregateProcessor aggregator, ScatterGatherResults results )
    {
        this.results = results;
        this.priceQuoteAggregator = aggregator;
        this.subscribers = new HashMap<>();
    }

    /* @see io.vlingo.reactive.messaging.patterns.scattergather.OrderProcessor#requestPriceQuoteSubscription(io.vlingo.reactive.messaging.patterns.scattergather.QuoteSubscriptionRequest) */
    @Override
    public void subscribe( QuoteSubscriptionRequest request )
    {
        logger().debug( String.format( "%s interested", request.quoteProcessor ));
        subscribers.put( request.quoterId, request );
        results.access.writeUsing("afterProcessorRegisteredCount", 1);
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.OrderProcessor#requestForQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
    @Override
    public void requestForQuote( RetailBasket basket )
    {
        priceQuoteAggregator.requiredPriceQuoteForFulfillment( basket.rfqId, subscribers.size() * basket.retailItems.size(), selfAs( OrderProcessor.class ));
        dispatch( basket );
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.OrderProcessor#remittedPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.PriceQuote) */
    @Override
    public void remittedPriceQuote( PriceQuote quote )
    {
        priceQuoteAggregator.priceQuoteFulfilled( quote );
    }

    /* @see io.vlingo.reactive.messaging.patterns.scattergather.OrderProcessor#bestPriceQuotation(io.vlingo.reactive.messaging.patterns.scattergather.QuotationFulfillment) */
    @Override
    public void bestPriceQuotation( BestPriceQuotation bestPriceQuotation )
    {
        logger().debug( String.format( "OrderProcessor received best quotes: %s", bestPriceQuotation ) );
        results.access.writeUsing("afterBestPriceQuotationRegisteredCount", 1);
    }
    
    protected void dispatch( RetailBasket basket )
    {
        for ( QuoteSubscriptionRequest quoteSubscriptionRequest : subscribers.values() )
        {
            for ( RetailItem item : basket.retailItems )
            {
                quoteSubscriptionRequest
                    .quoteProcessor
                    .requestPriceQuote( basket.rfqId, item.itemId, item.retailPrice, basket.totalRetailPrice );
            }
        }
    }
}
