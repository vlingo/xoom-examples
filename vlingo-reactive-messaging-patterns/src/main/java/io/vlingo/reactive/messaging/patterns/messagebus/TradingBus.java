// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.messagebus.exception.InvalidCommandIdException;
import io.vlingo.reactive.messaging.patterns.messagebus.exception.InvalidNotificationIdException;

/**
 * TradingBus is the nexus between the {@link StockTrader}, {@link PortfolioManager}, and 
 * {@link MarketAnalysisTools} systems providing an ability to register interest in specific types
 * of messages and then delivering these messages asynchronously to each registrant.
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class TradingBus 
extends AbstractTradingActor
{
    private final Map<String, Vector<CommandHandler>> commandHandlers = new HashMap<>();
    private final Map<String, Vector<NotificationInterest>> notificationInterests = new HashMap<>();
    
    public TradingBus( TestUntil until )
    {
        super( until );
    }
    
    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#executeBuyOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeBuyOrder( String portfolioId, String symbol, Integer quantity, Double price )
    {
        logger().log( String.format( "%s::executeBuyOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        Vector<CommandHandler> commandHandlerVector = commandHandlers.get( TradingProcessor.EXECUTE_BUY_ORDER );
        for ( CommandHandler commandHandler : commandHandlerVector )
        {
            commandHandler.tradingProcessor.executeBuyOrder( portfolioId, symbol, quantity, price );
        }
        
        until().happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#buyOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void buyOrderExecuted(String portfolioId, String symbol, Integer quantity, Double price)
    {
        logger().log( String.format( "%s::buyOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        Vector<NotificationInterest> notificationInterestVector = notificationInterests.get( TradingProcessor.BUY_ORDER_EXECUTED );
        for ( NotificationInterest notificationInterest : notificationInterestVector )
        {
            notificationInterest.tradingProcessor.buyOrderExecuted( portfolioId, symbol, quantity, price );
        }
        
        until().happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#executeSellOrder(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void executeSellOrder(String portfolioId, String symbol, Integer quantity, Double price)
    {
        logger().log( String.format( "%s::executeSellOrder( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        Vector<CommandHandler> commandHandlerVector = commandHandlers.get( TradingProcessor.EXECUTE_SELL_ORDER );
        for ( CommandHandler commandHandler : commandHandlerVector )
        {
            commandHandler.tradingProcessor.executeSellOrder( portfolioId, symbol, quantity, price );
        }
        
        until().happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#sellOrderExecuted(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Double) */
    @Override
    public void sellOrderExecuted(String portfolioId, String symbol, Integer quantity, Double price)
    {
        logger().log( String.format( "%s::sellOrderExecuted( %s, %s, %d, %.2f )", getClass().getSimpleName(), portfolioId, symbol, quantity, price ));
        Vector<NotificationInterest> notificationInterestVector = notificationInterests.get( TradingProcessor.SELL_ORDER_EXECUTED );
        for ( NotificationInterest notificationInterest : notificationInterestVector )
        {
            notificationInterest.tradingProcessor.sellOrderExecuted( portfolioId, symbol, quantity, price );
        }
        
        until().happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#registerCommandHandler(java.lang.String, java.lang.String, io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor) */
    @Override
    public void registerCommandHandler( String applicationId, String commandId, TradingProcessor handler )
    {
        validateCommandId( commandId );
        
        logger().log( String.format( "%s::registerCommandHandler( %s, %s, %s )", getClass().getSimpleName(), applicationId, commandId, handler.getClass().getSimpleName() ));
        CommandHandler commandHandler = new CommandHandler( commandId, applicationId, handler );
        Vector<CommandHandler> commandHandlerVector = commandHandlers.get( commandId );
        if ( Objects.isNull( commandHandlerVector ))
        {
            commandHandlerVector = new Vector<>();
            commandHandlers.put( commandId, commandHandlerVector );
        }
        
        commandHandlerVector.add( commandHandler );
        
        until().happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.AbstractTradingActor#registerNotificationInterest(java.lang.String, java.lang.String, io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor) */
    @Override
    public void registerNotificationInterest( String applicationId, String notificationId, TradingProcessor interested )
    {
        validateNotificationId( notificationId );
        
        logger().log( String.format( "%s::registerNotificationInterest( %s, %s, %s )", getClass().getSimpleName(), applicationId, notificationId, interested.getClass().getSimpleName() ));
        NotificationInterest notificationInterest = new NotificationInterest( notificationId, applicationId, interested );
        Vector<NotificationInterest> notificationInterestVector = notificationInterests.get( notificationId );
        if ( Objects.isNull( notificationInterestVector ))
        {
            notificationInterestVector = new Vector<>();
            notificationInterests.put( notificationId, notificationInterestVector );
        }
        
        notificationInterestVector.add( notificationInterest );
        
        until().happened();
    }

    // TESTING
    /**
     * @param commandId
     */
    protected void validateCommandId( String commandId )
    {
        if ( ! TradingProcessor.EXECUTE_BUY_ORDER.equals( commandId ) 
          && ! TradingProcessor.EXECUTE_SELL_ORDER.equals( commandId ))
        {
            throw new InvalidCommandIdException( String.format( "Unknown commandId %s", commandId ));
        }
    }

    /**
     * @param notificationId
     */
    protected void validateNotificationId( String notificationId )
    {
        if (    ! TradingProcessor.BUY_ORDER_EXECUTED.equals( notificationId )
           &&   ! TradingProcessor.SELL_ORDER_EXECUTED.equals( notificationId ))
        {
            throw new InvalidNotificationIdException( String.format( "Unknown notificaitonId %s", notificationId ));
        }
    }

}
