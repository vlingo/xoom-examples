// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

/**
 * StockTrader {@link ACtor} registers interest in buy and sell commands; performs the work the work related to
 * a buy or sell execution method invocation; and then publishes a notification that the specific 
 * buy or sell order was executed.
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class StockTrader 
extends Actor
implements TradingProcessor
{
    public final TestUntil until;
    public final TradingBusProcessor tradingBus;
    
    public StockTrader( TestUntil until, TradingBusProcessor tradingBus )
    {
        this.until = until;
        this.tradingBus = tradingBus;
    }

    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        String applicationId = this.getClass().getSimpleName();
        tradingBus.registerHandler( new RegisterCommandHandler( applicationId, TradingProcessor.EXECUTE_BUY_ORDER, this ));
        tradingBus.registerHandler( new RegisterCommandHandler( applicationId, TradingProcessor.EXECUTE_SELL_ORDER, this ));
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#executeBuyOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeBuyOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( String.format( "%s::executeBuyOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));

        /*
         * execute buy order work to be performed here
         */
        
        tradingBus.notify( new TradingNotification( TradingProcessor.BUY_ORDER_EXECUTED, portfolioId, symbol, quantity, price ));
        
        until.happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#executeSellOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeSellOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( String.format( "%s::executeSellOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * execute sell order work to be performed here
         */
        
        tradingBus.notify( new TradingNotification( TradingProcessor.SELL_ORDER_EXECUTED, portfolioId, symbol, quantity, price ));
        
        until.happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( "Unsupported method buyOrderExecuted" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( "Unsupported method sellOrderExecuted" );
    }

}
