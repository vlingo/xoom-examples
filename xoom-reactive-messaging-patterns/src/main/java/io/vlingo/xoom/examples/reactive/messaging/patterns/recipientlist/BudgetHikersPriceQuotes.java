// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.recipientlist;

import io.vlingo.xoom.actors.Actor;

/**
 * BudgetHikersPriceQuotes {@link QuoteProcessor} {@link Actor} quote processor responsible for Budget Hikers.
 */
public class BudgetHikersPriceQuotes 
extends Actor 
implements QuoteProcessor
{
    public final OrderProcessor orderProcessor;
    private final RecipientListResults results;
    
    public BudgetHikersPriceQuotes( final OrderProcessor orderProcessor, final RecipientListResults results )
    {
        this.orderProcessor = orderProcessor;
        this.results = results;
    }

    /* @see io.vlingo.xoom.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        orderProcessor.register( new PriceQuoteInterest( this.getClass().getName(), selfAs( QuoteProcessor.class ), 1d, 1000d ));
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.xoom.examples.reactive.messaging.patterns.recipientlist.RetailBasket) */
    @Override
    public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
    {
        Double discountPercentage = discountPercentage( totalRetailPrice );
        Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
        orderProcessor.remittedPriceQuote( new PriceQuote( selfAs( QuoteProcessor.class ), rfqId, itemId, retailPrice, discountPrice ));
        results.access.writeUsing("afterQuotationReceivedAtBudgetHikersCount", 1);
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
