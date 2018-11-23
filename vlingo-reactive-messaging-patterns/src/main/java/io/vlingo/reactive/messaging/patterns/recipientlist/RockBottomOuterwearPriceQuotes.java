// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

import io.vlingo.actors.Actor;

/**
 * RockBottomOuterwearPriceQuotes {@link QuoteProcessor} {@link Actor} quote processor responsible for Rock Bottom Outerwear.
 */
public class RockBottomOuterwearPriceQuotes 
extends Actor 
implements QuoteProcessor
{
    public final OrderProcessor orderProcessor;
    
    public RockBottomOuterwearPriceQuotes( final OrderProcessor orderProcessor )
    {
        this.orderProcessor = orderProcessor;
    }
    
    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        orderProcessor.register( new PriceQuoteInterest( getClass().getName(), selfAs( QuoteProcessor.class ), .50, 7500d ));
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
        if ( orderTotalRetailprice <= 100.00 ) discount = .015;
        else if ( orderTotalRetailprice <= 399.99 ) discount = .02;
        else if ( orderTotalRetailprice <= 499.99 ) discount = .03;
        else if ( orderTotalRetailprice <= 799.99 ) discount = .04;
        else if ( orderTotalRetailprice <= 999.99 ) discount = .05;
        else if ( orderTotalRetailprice <= 2999.99 ) discount = .06;
        else if ( orderTotalRetailprice <= 4999.99 ) discount = 0.07;
        else if ( orderTotalRetailprice <= 5999.99 ) discount = 0.075;
        else discount = .08;
        return discount;
    }
}
