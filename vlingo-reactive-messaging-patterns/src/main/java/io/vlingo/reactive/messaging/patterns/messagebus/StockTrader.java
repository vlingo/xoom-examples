// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import io.vlingo.actors.testkit.TestUntil;

/**
 * StockTrader
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class StockTrader 
extends AbstractTradingActor
{
    public StockTrader( TestUntil until, TradingProcessor tradingBus )
    {
        super( until, tradingBus );
    }

    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        super.beforeStart();
        tradingBus().registerCommandHandler( applicationId(), TradingProcessor.EXECUTE_BUY_ORDER, this );
        tradingBus().registerCommandHandler( applicationId(), TradingProcessor.EXECUTE_SELL_ORDER, this );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#executeBuyOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeBuyOrder(String portfolioId, String symbol, Integer quantity, Double price)
    {
        logger().log( String.format( "%s::executeBuyOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        tradingBus().buyOrderExecuted( portfolioId, symbol, quantity, price );
        
        until().happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#executeSellOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeSellOrder(String portfolioId, String symbol, Integer quantity, Double price)
    {
        logger().log( String.format( "%s::executeSellOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        tradingBus().sellOrderExecuted( portfolioId, symbol, quantity, price );
        
        until().happened();
    }

}
