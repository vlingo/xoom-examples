// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * NotificationInterest
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class NotificationInterest
implements TradingProcessor
{
    public final String notificationId;
    public final String applicationId;
    public final TradingProcessor tradingProcessor;
    
    public NotificationInterest( String notificationId, String applicationId, TradingProcessor tradingProcessor )
    {
        this.notificationId = notificationId;
        this.applicationId = applicationId;
        this.tradingProcessor = tradingProcessor;
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#executeBuyOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeBuyOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        // unimplemented
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#executeSellOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeSellOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        // unimplemented
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        if ( isInterestedInBuyOrderExecuted() )
            tradingProcessor.buyOrderExecuted( portfolioId, symbol, quantity, price );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price )
    {
        if ( isInterestedInSellOrderExecuted() )
            tradingProcessor.sellOrderExecuted( portfolioId, symbol, quantity, price );
    }

    // TESTING
    public boolean isInterestedInBuyOrderExecuted()
    {
        return TradingProcessor.BUY_ORDER_EXECUTED.equals( notificationId );
    }

    public boolean isInterestedInSellOrderExecuted()
    {
        return TradingProcessor.SELL_ORDER_EXECUTED.equals( notificationId );
    }
    
}
