// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import io.vlingo.actors.testkit.TestUntil;

/**
 * MarketAnalysisTools
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class MarketAnalysisTools 
extends AbstractTradingActor
{
    public MarketAnalysisTools( TestUntil until, TradingProcessor tradingBus )
    {
        super( until, tradingBus );
    }

    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        super.beforeStart();
        tradingBus().registerNotificationInterest( applicationId(), TradingProcessor.BUY_ORDER_EXECUTED, this );
        tradingBus().registerNotificationInterest( applicationId(), TradingProcessor.SELL_ORDER_EXECUTED, this );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted(String portfolioId, String symbol, Integer quantity, Double price)
    {
        logger().log( String.format( "%s::buyOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * perform buy order executed analysis work here
         */
        until().happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted(String portfolioId, String symbol, Integer quantity, Double price)
    {
        logger().log( String.format( "%s::sellOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * perform sell order executed analysis work here
         */
        until().happened();
    }

}
