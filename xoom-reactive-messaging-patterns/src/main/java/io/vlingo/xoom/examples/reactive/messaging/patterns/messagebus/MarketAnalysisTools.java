// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.TradingBusCommands.RegisterNotificationInterest;

/**
 * MarketAnalysisTools {@link Actor} registers interest in specific notifications related to analysis
 * and provides the behavior to be executed when these notifications arrive.
 */
public class MarketAnalysisTools 
extends Actor
implements TradingProcessor
{
    public final TradingBusResults tradingBusResults;
    public final TradingBusProcessor tradingBus;
    
    public MarketAnalysisTools( TradingBusResults tradingBusResults, TradingBusProcessor tradingBus )
    {
        this.tradingBusResults = tradingBusResults;
        this.tradingBus = tradingBus;
    }

    /* @see io.vlingo.xoom.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        String applicationId = this.getClass().getSimpleName();
        tradingBus.registerInterest( new RegisterNotificationInterest( applicationId, TradingProcessor.BUY_ORDER_EXECUTED, this ));
        tradingBus.registerInterest( new RegisterNotificationInterest( applicationId, TradingProcessor.SELL_ORDER_EXECUTED, this ));
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.AbstractTradingActor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().debug( String.format( "%s::buyOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * perform buy order executed analysis work here
         */

        tradingBusResults.access.writeUsing("afterMarketAnalysisBuyOrderExecutedCount", 1);
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.AbstractTradingActor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().debug( String.format( "%s::sellOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * perform sell order executed analysis work here
         */

        tradingBusResults.access.writeUsing("afterMarketAnalysisSellOrderExecutedCount", 1);
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.TradingProcessor#executeBuyOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeBuyOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().debug( "Unsupported method executeBuyOrder" );
    }

    @Override
    public void executeSellOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().debug( "Unsupported method executeSellOrder" );
    }

}
