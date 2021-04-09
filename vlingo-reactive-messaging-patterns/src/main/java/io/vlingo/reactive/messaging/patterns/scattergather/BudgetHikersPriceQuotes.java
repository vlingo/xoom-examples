// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import io.vlingo.actors.Actor;

/**
 * BudgetHikersPriceQuotes {@link QuoteProcessor} {@link Actor} representing Budget Hikers.
 */
public class BudgetHikersPriceQuotes 
extends Actor 
implements QuoteProcessor
{
    public final OrderProcessor orderProcessor;
    
    public BudgetHikersPriceQuotes( final OrderProcessor orderProcessor )
    {
        this.orderProcessor = orderProcessor;
    }

    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        orderProcessor.subscribe( new QuoteSubscriptionRequest( this.getClass().getName(), selfAs( QuoteProcessor.class ) ));
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
    @Override
    public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
    {
        Double discountPercentage = discountPercentage( totalRetailPrice );
        Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
        orderProcessor.remittedPriceQuote( new PriceQuote( selfAs( QuoteProcessor.class ), rfqId, itemId, retailPrice, discountPrice ));
    }

    protected Double discountPercentage( Double orderTotalRetailprice )
    {
        Double discount = null;
        if ( orderTotalRetailprice <= 100.00 ) discount = .02;
        else if ( orderTotalRetailprice <= 399.99 ) discount = .03;
        else if ( orderTotalRetailprice <= 499.99 ) discount = .05;
        else if ( orderTotalRetailprice <= 799.99 ) discount = .07;
        else discount = .075;
        return discount;
    }
}
