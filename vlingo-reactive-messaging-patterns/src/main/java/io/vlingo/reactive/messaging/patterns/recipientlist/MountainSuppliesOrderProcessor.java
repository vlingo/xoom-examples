// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.recipientlist.PriceQuoteInterest.PriceQuote;
import io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket.RetailItem;

/**
 * MountainSuppliesOrderProcessor maintains registry of @{@link QuoteProcessor} {@link Actor} instances
 * interested in providing quotes according to constraints on total retail price of a basket of items. 
 *
 * @author brsg.io
 * @since Nov 20, 2018
 */
public class MountainSuppliesOrderProcessor 
extends Actor 
implements OrderProcessor
{
    public final TestUntil until;
    public final TestUntil untilRegistered;
    public final Map<String, PriceQuoteInterest> interestRegistry;
    
    public MountainSuppliesOrderProcessor( TestUntil until, TestUntil untilRegistered )
    {
        this.until = until;
        this.untilRegistered = untilRegistered;
        this.interestRegistry = new HashMap<>();
    }

    /* @see io.vlingo.reactive.messaging.patterns.recipientlist.OrderProcessor#registerPriceQuoteInterest(io.vlingo.reactive.messaging.patterns.recipientlist.PriceQuoteInterest) */
    @Override
    public void register( PriceQuoteInterest interest )
    {
        logger().log( String.format( "%s interested", interest.path ));
        interestRegistry.put( interest.path, interest );
        untilRegistered.happened();
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
        logger().log( String.format( "OrderProcessor received price quote: %s", quote ));
        until.happened();
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

    // QUOTE PROCESSORS
    public static class BudgetHikersPriceQuotes 
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
            orderProcessor.register( new PriceQuoteInterest( this.getClass().getName(), this, 1d, 1000d ));
        }

        /* @see io.vlingo.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
        @Override
        public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
        {
            Double discountPercentage = discountPercentage( totalRetailPrice );
            Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
            orderProcessor.remittedPriceQuote( new PriceQuote( this, rfqId, itemId, retailPrice, discountPrice ));
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

    public static class HighSierraPriceQuotes 
    extends Actor 
    implements QuoteProcessor
    {
        public final OrderProcessor orderProcessor;
        
        public HighSierraPriceQuotes( final OrderProcessor orderProcessor )
        {
            this.orderProcessor = orderProcessor;
        }

        /* @see io.vlingo.actors.Actor#beforeStart() */
        @Override
        protected void beforeStart()
        {
            orderProcessor.register( new PriceQuoteInterest( this.getClass().getName(), this, 100d, 10000d ));
        }

        /* @see io.vlingo.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
        @Override
        public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
        {
            Double discountPercentage = discountPercentage( totalRetailPrice );
            Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
            orderProcessor.remittedPriceQuote( new PriceQuote( this, rfqId, itemId, retailPrice, discountPrice ));
        }

        protected Double discountPercentage( Double orderTotalRetailprice )
        {
            Double discount = null;
            if ( orderTotalRetailprice <= 150.00 ) discount = .015;
            else if ( orderTotalRetailprice <= 499.99 ) discount = .02;
            else if ( orderTotalRetailprice <= 999.99 ) discount = .03;
            else if ( orderTotalRetailprice <= 4999.99 ) discount = .04;
            else discount = .05;
            return discount;
        }
    }
    
    public static class MountainAscentPriceQuotes 
    extends Actor 
    implements QuoteProcessor
    {
        public final OrderProcessor orderProcessor;
        
        public MountainAscentPriceQuotes( final OrderProcessor orderProcessor )
        {
            this.orderProcessor = orderProcessor;
        }

        /* @see io.vlingo.actors.Actor#beforeStart() */
        @Override
        protected void beforeStart()
        {
            orderProcessor.register( new PriceQuoteInterest( getClass().getName(), this, 70d, 500d ));
        }

        /* @see io.vlingo.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
        @Override
        public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
        {
            Double discountPercentage = discountPercentage( totalRetailPrice );
            Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
            orderProcessor.remittedPriceQuote( new PriceQuote( this, rfqId, itemId, retailPrice, discountPrice ));
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
    
    public static class PinnacleGearPriceQuotes 
    extends Actor 
    implements QuoteProcessor
    {
        public final OrderProcessor orderProcessor;
        
        public PinnacleGearPriceQuotes( final OrderProcessor orderProcessor )
        {
            this.orderProcessor = orderProcessor;
        }

        /* @see io.vlingo.actors.Actor#beforeStart() */
        @Override
        protected void beforeStart()
        {
            orderProcessor.register( new PriceQuoteInterest( getClass().getName(), this, 250d, 500000d ));
        }

        /* @see io.vlingo.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
        @Override
        public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
        {
            Double discountPercentage = discountPercentage( totalRetailPrice );
            Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
            orderProcessor.remittedPriceQuote( new PriceQuote( this, rfqId, itemId, retailPrice, discountPrice ));
        }

        protected Double discountPercentage( Double orderTotalRetailprice )
        {
            Double discount = null;
            if ( orderTotalRetailprice <= 299.99 ) discount = .015;
            else if ( orderTotalRetailprice <= 399.99 ) discount = .0175;
            else if ( orderTotalRetailprice <= 499.99 ) discount = .02;
            else if ( orderTotalRetailprice <= 999.99 ) discount = .03;
            else if ( orderTotalRetailprice <= 1199.99 ) discount = .035;
            else if ( orderTotalRetailprice <= 4999.99 ) discount = 0.04;
            else if ( orderTotalRetailprice <= 7999.99 ) discount = 0.05;
            else discount = .06;
            return discount;
        }
    }

    public static class RockBottomOuterwearPriceQuotes 
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
            orderProcessor.register( new PriceQuoteInterest( getClass().getName(), this, .50, 7500d ));
        }

        /* @see io.vlingo.reactive.messaging.patterns.recipientlist.QuoteProcessor#requestPriceQuote(io.vlingo.reactive.messaging.patterns.recipientlist.RetailBasket) */
        @Override
        public void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice )
        {
            Double discountPercentage = discountPercentage( totalRetailPrice );
            Double discountPrice = retailPrice - ( retailPrice * discountPercentage );
            orderProcessor.remittedPriceQuote( new PriceQuote( this, rfqId, itemId, retailPrice, discountPrice ));
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
    
}
