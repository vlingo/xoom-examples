// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.messagebus.exception.UnexpectedMethodInvocationException;

/**
 * AbstractTradingActor
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public abstract class AbstractTradingActor 
extends Actor 
implements TradingProcessor
{
    private final TestUntil until;
    private TradingProcessor tradingBus;
    private final String applicationId = this.getClass().getSimpleName();
    
    public AbstractTradingActor( TestUntil until )
    {
        this.until = until;
        initialize();
    }
    
    public AbstractTradingActor( TestUntil until, TradingProcessor tradingBus )
    {
        this.until = until;
        this.tradingBus = tradingBus;
        initialize();
    }
    
    abstract void initialize();

    /**
     * Returns the value of {@link #until}
     *
     * @return the value of {@link #until}
     */
    public TestUntil getUntil()
    {
        return until;
    }

    /**
     * Returns the value of {@link #tradingBus}
     *
     * @return the value of {@link #tradingBus}
     */
    public TradingProcessor getTradingBus()
    {
        return tradingBus;
    }

    /**
     * Returns the value of {@link #applicationId}
     *
     * @return the value of {@link #applicationId}
     */
    public String getApplicationId()
    {
        return applicationId;
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#executeBuyOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeBuyOrder(String portfolioId, String symbol, Integer quantity, Double price)
    {
        throw new UnexpectedMethodInvocationException( "executeBuyOrder unexpectedly called" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted(String portfolioId, String symbol, Integer quantity, Double price)
    {
        throw new UnexpectedMethodInvocationException( "buyOrderExecuted unexpectedly called" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#executeSellOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeSellOrder(String portfolioId, String symbol, Integer quantity, Double price)
    {
        throw new UnexpectedMethodInvocationException( "executeSellOrder unexpectedly called" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted(String portfolioId, String symbol, Integer quantity, Double price)
    {
        throw new UnexpectedMethodInvocationException( "sellOrderExecuted unexpectedly called" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#registerCommandHandler(java.lang.String, java.lang.String, io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor) */
    @Override
    public void registerCommandHandler(String applicationId, String commandId, TradingProcessor handler)
    {
        throw new UnexpectedMethodInvocationException( "registerCommandHandler unexpectedly called" );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor#registerNotificationInterest(java.lang.String, java.lang.String, io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor) */
    @Override
    public void registerNotificationInterest(String applicationId, String notificationId, TradingProcessor interested)
    {
        throw new UnexpectedMethodInvocationException( "registerNotificationInterest unexpectedly called" );
    }

}
