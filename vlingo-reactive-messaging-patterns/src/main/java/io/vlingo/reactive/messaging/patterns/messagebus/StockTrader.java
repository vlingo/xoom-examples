// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import io.vlingo.actors.Actor;
import io.vlingo.reactive.messaging.patterns.messagebus.TradingBusCommands.RegisterCommandHandler;
import io.vlingo.reactive.messaging.patterns.messagebus.TradingBusCommands.TradingNotification;

/**
 * StockTrader {@link Actor} registers interest in buy and sell commands; performs the work the work related to
 * a buy or sell execution method invocation; and then publishes a notification that the specific 
 * buy or sell order was executed.
 */
public class StockTrader 
extends Actor
implements TradingProcessor
{
    public final TradingBusResults tradingBusResults;
    public final TradingBusProcessor tradingBus;
    
    public StockTrader( TradingBusResults tradingBusResults, TradingBusProcessor tradingBus )
    {
        this.tradingBusResults = tradingBusResults;
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
        logger().debug( String.format( "%s::executeBuyOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));

        /*
         * execute buy order work to be performed here
         */
        
        tradingBus.notify( new TradingNotification( TradingProcessor.BUY_ORDER_EXECUTED, portfolioId, symbol, quantity, price ));

        tradingBusResults.access.writeUsing("afterStockTraderBuyOrderExecutedCount", 1);
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#executeSellOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeSellOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().debug( String.format( "%s::executeSellOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * execute sell order work to be performed here
         */
        
        tradingBus.notify( new TradingNotification( TradingProcessor.SELL_ORDER_EXECUTED, portfolioId, symbol, quantity, price ));

        tradingBusResults.access.writeUsing("afterStockTraderSellOrderExecutedCount", 1);
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().debug( "Unsupported method buyOrderExecuted" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().debug( "Unsupported method sellOrderExecuted" );
    }

}
