// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.messagebus.TradingBusCommands.RegisterNotificationInterest;

/**
 * PortfolioManager {@link Actor} registers interest in specific notifications and waits to receive 
 * notifications of this type to perform work related to the notification.
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class PortfolioManager 
extends Actor
implements TradingProcessor
{
    public final TestUntil until;
    public final TradingBusProcessor tradingBus;

    public PortfolioManager( TestUntil until, TradingBusProcessor tradingBus )
    {
        this.until = until;
        this.tradingBus = tradingBus;
    }

    /* @see io.vlingo.actors.Actor#beforeStart() */
    @Override
    protected void beforeStart()
    {
        String applicationId = this.getClass().getSimpleName();
        tradingBus.registerInterest( new RegisterNotificationInterest( applicationId, TradingProcessor.BUY_ORDER_EXECUTED, this ));
        tradingBus.registerInterest( new RegisterNotificationInterest( applicationId, TradingProcessor.SELL_ORDER_EXECUTED, this ));
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( String.format( "%s::buyOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * perform buy order executed analysis work here
         */
        until.happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    { 
        logger().log( String.format( "%s::sellOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        
        /*
         * perform sell order executed analysis work here
         */
        until.happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#executeBuyOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeBuyOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( "Unsupported method executeBuyOrder" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#executeSellOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeSellOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( "Unsupported method executeSellOrder" );
    }

}
