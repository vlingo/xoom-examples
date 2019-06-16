// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import org.junit.Assert;
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;

/**
 * ScatterGatherTest driver for this scatter-gather example.
 */
public class ScatterGatherTest
{
    public static final String WORLD_NAME = "scatter-gather-example";

    @Test
    public void testScatterGatherRuns()
    {
        World world = World.startWithDefaults( WORLD_NAME );
        world.defaultLogger().debug( "ScatterGatherTest: is started" );
        
        final ScatterGatherResults results = new ScatterGatherResults();
        final AccessSafely access = results.afterCompleting(10);
        
        AggregateProcessor priceQuoteAggregator = world.actorFor( AggregateProcessor.class, PriceQuoteAggregator.class );
        OrderProcessor mtnSuppliesOrderProcessor = world.actorFor( OrderProcessor.class, MountainSuppliesOrderProcessor.class, priceQuoteAggregator, results );
        world.actorFor( QuoteProcessor.class, BudgetHikersPriceQuotes.class, mtnSuppliesOrderProcessor );
        world.actorFor( QuoteProcessor.class, HighSierraPriceQuotes.class, mtnSuppliesOrderProcessor );
        world.actorFor( QuoteProcessor.class, MountainAscentPriceQuotes.class, mtnSuppliesOrderProcessor );
        world.actorFor( QuoteProcessor.class, PinnacleGearPriceQuotes.class, mtnSuppliesOrderProcessor );
        world.actorFor( QuoteProcessor.class, RockBottomOuterwearPriceQuotes.class, mtnSuppliesOrderProcessor );
        
        world.defaultLogger().debug("Register completes!!!");
        
        mtnSuppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "123", 
                    new RetailItem( "1", 29.95 ), 
                    new RetailItem( "2", 99.95 ), 
                    new RetailItem( "3", 14.95 )
                )
            );
        
        mtnSuppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "125", 
                    new RetailItem( "4", 39.95 ), 
                    new RetailItem( "5", 199.95 ), 
                    new RetailItem( "6", 149.95 ),
                    new RetailItem( "7", 724.99 )
                )
            );
    
        mtnSuppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "129", 
                    new RetailItem( "8", 119.99 ), 
                    new RetailItem( "9", 499.95 ), 
                    new RetailItem( "10", 519.00 ),
                    new RetailItem( "11", 209.50 )
                )
            );
    
        mtnSuppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "135", 
                    new RetailItem( "12", .97 ), 
                    new RetailItem( "13", 9.50 ), 
                    new RetailItem( "14", 1.99 )
                )
            );
    
        mtnSuppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "140", 
                    new RetailItem( "15", 107.50 ), 
                    new RetailItem( "16", 9.50 ), 
                    new RetailItem( "17", 599.99 ),
                    new RetailItem( "18", 249.95 ),
                    new RetailItem( "19", 789.99 )
                )
            );

        Assert.assertEquals(5, (int) access.readFrom("afterProcessorRegisteredCount"));
        Assert.assertEquals(5, (int) access.readFrom("afterBestPriceQuotationRegisteredCount"));

        world.defaultLogger().debug( "ScatterGatherTest: is completed" );
        world.terminate();
    }

}
