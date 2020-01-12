// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

import io.vlingo.actors.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MountainSuppliesOrderProcessor maintains registry of @{@link QuoteProcessor} {@link Actor} instances
 * interested in providing quotes according to constraints on total retail price of a basket of items. 
 */
public class MountainSuppliesOrderProcessor 
extends Actor 
implements OrderProcessor
{
    public final RecipientListResults results;
    public final Map<String, PriceQuoteInterest> interestRegistry;
    
    public MountainSuppliesOrderProcessor( final RecipientListResults results )
    {
        this.results = results;
        this.interestRegistry = new HashMap<>();
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.OrderProcessor#registerPriceQuoteInterest(io.vlingo.reactive.messaging.patterns.recipientlist.PriceQuoteInterest) */
    @Override
    public void register( PriceQuoteInterest interest )
    {
        logger().debug( String.format( "%s interested", interest.type ));
        interestRegistry.put( interest.type, interest );
        results.access.writeUsing("afterProcessorRegistered", 1);
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.OrderProcessor#requestForQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
    @Override
    public void requestForQuote( RetailBasket basket )
    {
        List<QuoteProcessor> recipientList = calculateRecipientList( basket );
        dispatchTo( basket, recipientList );
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.OrderProcessor#remittedPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.PriceQuote) */
    @Override
    public void remittedPriceQuote( PriceQuote quote )
    {
        logger().debug( String.format( "OrderProcessor received price quote: %s", quote ));
        results.access.writeUsing("afterQuotationRemitted", 1);
    }

    protected List<QuoteProcessor> calculateRecipientList( RetailBasket basket )
    {
        List<QuoteProcessor> recipientList = new ArrayList<>();
        for ( PriceQuoteInterest interest : interestRegistry.values() )
        {
            if (( interest.lowTotalRetail <= basket.totalRetailPrice )
             && ( interest.highTotalRetail >= basket.totalRetailPrice ))
            {
                recipientList.add( interest.quoteProcessor );
            }
        }
        return recipientList;
    }

    protected void dispatchTo( RetailBasket basket, List<QuoteProcessor> recipientList )
    {
        for ( QuoteProcessor quoteProcessor : recipientList )
        {
            for ( RetailItem item : basket.retailItems )
            {
                quoteProcessor.requestPriceQuote( basket.rfqId, item.itemId, item.retailPrice, basket.totalRetailPrice );
            }
        }
    }
}
