// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

import io.vlingo.actors.Actor;

/**
 * MountainAscentPriceQuotes {@link QuoteProcessor} {@link Actor} quote processor responsible for Mountain Ascent.
 */
public class MountainAscentPriceQuotes extends Actor implements QuoteProcessor
{
    public final OrderProcessor orderProcessor;
    private final RecipientListResults results;
    
    public MountainAscentPriceQuotes( final OrderProcessor orderProcessor, final RecipientListResults results )
    {
        this.orderProcessor = orderProcessor;
        this.results = results;
    }

    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        orderProcessor.register( new PriceQuoteInterest( getClass().getName(), selfAs( QuoteProcessor.class ), 70d, 500d ));
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
    @Override
    public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
    {
        Double discountPercentage = discountPercentage( totalRetailPrice );
        Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
        orderProcessor.remittedPriceQuote( new PriceQuote( selfAs( QuoteProcessor.class ), rfqId, itemId, retailPrice, discountPrice ));
        results.access.writeUsing("afterQuotationReceivedAtMountainAscentCount", 1);
    }

    protected Double discountPercentage( Double orderTotalRetailprice )
    {
        Double discount = null;
        if ( orderTotalRetailprice <= 99.99 ) discount = .01;
        else if ( orderTotalRetailprice <= 199.99 ) discount = .02;
        else if ( orderTotalRetailprice <= 499.99 ) discount = .03;
        else if ( orderTotalRetailprice <= 799.99 ) discount = .04;
        else if ( orderTotalRetailprice <= 999.99 ) discount = .045;
        else if ( orderTotalRetailprice <= 2999.99 ) discount = .0475;
        else discount = .05;
        return discount;
    }
}
