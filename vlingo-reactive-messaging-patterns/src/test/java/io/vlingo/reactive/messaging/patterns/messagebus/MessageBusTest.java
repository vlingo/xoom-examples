// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import static org.junit.Assert.*;

import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

/**
 * MessageBusTest demonstrates the {@link TradingBus} actor providing a listener service that allows three
 * {@link Actor} classes, {@link StockTrader}, {@link PortfolioManager}, and {@link MarketAnalysisTools}, 
 * to represent disparate systems that have an interest in a subset of the messages received by the 
 * TradingBus, which happily forwards the relevant messages asynchronously to these actors for their own 
 * individual processing.
 *
 * @author brsg.io
 * @since Oct 26, 2018
 */
public class MessageBusTest
{

    public static final int EVENTS = 24;
    public static final String WORLD_NAME = "message-bus-example";

    @Test
    public void testMessageBusRuns()
    {
        World world = World.startWithDefaults( WORLD_NAME );
        
        world.defaultLogger().log( "TradingBus: is started" );
        
        TestUntil until = TestUntil.happenings( EVENTS );
        
        final TradingBusProcessor tradingBus = world.actorFor( Definition.has( TradingBus.class, Definition.parameters( until )), TradingBusProcessor.class );
        
        @SuppressWarnings("unused")
        final TradingProcessor stockTrader = world.actorFor( Definition.has( StockTrader.class, Definition.parameters( until, tradingBus )), TradingProcessor.class );
        @SuppressWarnings("unused")
        final TradingProcessor portfolioManager = world.actorFor( Definition.has( PortfolioManager.class, Definition.parameters( until, tradingBus )), TradingProcessor.class );
        @SuppressWarnings("unused")
        final TradingProcessor marketAnalysisTool = world.actorFor( Definition.has(  MarketAnalysisTools.class, Definition.parameters( until, tradingBus )), TradingProcessor.class );
        
        tradingBus.trade( new TradingCommand( TradingProcessor.EXECUTE_BUY_ORDER, "p123", "MSFT", 100, 31.85 ));
        tradingBus.trade( new TradingCommand( TradingProcessor.EXECUTE_SELL_ORDER, "p456", "MSFT", 200, 31.80 ));
        tradingBus.trade( new TradingCommand( TradingProcessor.EXECUTE_BUY_ORDER, "p789", "MSFT", 100, 31.83 ));
        
        until.completes();
        
        world.defaultLogger().log( "TradingBus: is completed" );
        
        world.terminate();
    }

}
