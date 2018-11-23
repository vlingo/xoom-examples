// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.common.Scheduled;
import io.vlingo.reactive.messaging.patterns.scattergather.BestPriceQuotation.PriceQuote;
import io.vlingo.reactive.messaging.patterns.scattergather.BestPriceQuotation.PriceQuoteTimedOut;
import io.vlingo.reactive.messaging.patterns.scattergather.BestPriceQuotation.QuotationFulfillment;
import io.vlingo.reactive.messaging.patterns.scattergather.RetailBasket.RetailItem;

/**
 * MountainSuppliesOrderProcessor maintains registry of @{@link QuoteProcessor} {@link Actor} instances
 * interested in providing quotes according to constraints on total retail price of a basket of items. 
 */
public class MountainSuppliesOrderProcessor 
extends Actor 
implements OrderProcessor
{
    public final AggregateProcessor priceQuoteAggregator;
    public final TestUntil until;
    public final TestUntil untilRegistered;
    public final Map<String, QuoteSubscriptionRequest> subscribers;
    
    public MountainSuppliesOrderProcessor( AggregateProcessor aggregator, TestUntil until, TestUntil untilRegistered )
    {
        this.priceQuoteAggregator = aggregator;
        this.until = until;
        this.untilRegistered = untilRegistered;
        this.subscribers = new HashMap<>();
    }

    /* @see io.vlingo.reactive.messaging.patterns.scattergather.OrderProcessor#requestPriceQuoteSubscription(io.vlingo.reactive.messaging.patterns.scattergather.QuoteSubscriptionRequest) */
    @Override
    public void subscribe( QuoteSubscriptionRequest request )
    {
        logger().log( String.format( "%s interested", request.quoteProcessor ));
        subscribers.put( request.quoterId, request );
        untilRegistered.happened();
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
        logger().log( String.format( "OrderProcessor received best quotes: %s", bestPriceQuotation ) );
        until.happened();
        
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
    
    // AGGREGATE PROCESSOR
    public static class PriceQuoteAggregator
    extends Actor
    implements AggregateProcessor
    {
        public final Map<String, QuotationFulfillment> fulfilledPriceQuotes;
        
        public PriceQuoteAggregator()
        {
            this.fulfilledPriceQuotes = new HashMap<>();
        }

        /* @see io.vlingo.reactive.messaging.patterns.scattergather.AggregateProcessor#requiredPriceQuoteForFulfillment(java.lang.String, java.lang.Integer, io.vlingo.reactive.messaging.patterns.scattergather.OrderProcessor) */
        @Override
        public void requiredPriceQuoteForFulfillment( String rfqId, Integer quotesRequested, OrderProcessor orderProcessor )
        {
            QuotationFulfillment quotationFulfillment = new QuotationFulfillment( rfqId, quotesRequested, orderProcessor );
            fulfilledPriceQuotes.put( rfqId, quotationFulfillment );
            Scheduled scheduled = new Scheduled()
            {
                @Override
                public void intervalSignal(Scheduled scheduled, Object data)
                {
                    String rfqId = (String) data;
                    priceQuoteTimedOut( new PriceQuoteTimedOut( rfqId ));
                }
            };
            this.scheduler().scheduleOnce( scheduled, rfqId, 2000L, 0L );
        }

        /* @see io.vlingo.reactive.messaging.patterns.scattergather.AggregateProcessor#priceQuoteFulfilled(io.vlingo.reactive.messaging.patterns.scattergather.PriceQuoteInterest.PriceQuote) */
        @Override
        public void priceQuoteFulfilled( PriceQuote priceQuote )
        {
            priceQuoteRequestFulfilled( priceQuote );
        }

        /* @see io.vlingo.reactive.messaging.patterns.scattergather.AggregateProcessor#priceQuoteTimedOut(io.vlingo.reactive.messaging.patterns.scattergather.PriceQuoteTimedOut) */
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
            orderProcessor.subscribe( new QuoteSubscriptionRequest( this.getClass().getName(), selfAs( QuoteProcessor.class )));
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
            orderProcessor.subscribe( new QuoteSubscriptionRequest( getClass().getName(), selfAs( QuoteProcessor.class )));
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
            orderProcessor.subscribe( new QuoteSubscriptionRequest( getClass().getName(), selfAs( QuoteProcessor.class )));
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
            orderProcessor.subscribe( new QuoteSubscriptionRequest( getClass().getName(), selfAs( QuoteProcessor.class )));
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

}
