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

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

/**
 * TradingBus is the nexus between the {@link StockTrader}, {@link PortfolioManager}, and 
 * {@link MarketAnalysisTools} systems providing an ability to register interest in specific types
 * of messages and then delivering these messages asynchronously to each registrant.
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class TradingBus 
extends Actor
implements TradingBusProcessor
{
    public final TestUntil until;
    private final Map<String, Vector<TradingProcessor>> commandHandlers = new HashMap<>();
    private final Map<String, Vector<TradingProcessor>> notificationInterests = new HashMap<>();
    
    public TradingBus( TestUntil until )
    {
        this.until = until;
    }
    
    // ACTTION
    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingBusProcessor#trade(io.vlingo.reactive.messaging.patterns.messagebus.TradingCommand) */
    @Override
    public void trade( TradingCommand command )
    {
        dispatchCommand( command );
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingBusProcessor#notify(io.vlingo.reactive.messaging.patterns.messagebus.TradingNotification) */
    @Override
    public void notify( TradingNotification notification )
    {
        dispatchNotification( notification );
    }

    protected void dispatchCommand( TradingCommand command )
    {
        Vector<TradingProcessor> commandHandlerVector = commandHandlers.get( command.commandId );
        for ( TradingProcessor handler : commandHandlerVector )
        {
            unwrapCommandAndForward( command, handler );
            
            until.happened();
        }
    }

    protected void unwrapCommandAndForward( TradingCommand command, TradingProcessor handler )
    {
        if ( command.commandId.equals( TradingProcessor.EXECUTE_BUY_ORDER ))
        {
            handler.executeBuyOrder( command.portfolioId, command.symbol, command.quantity, command.price );
        }
        else if ( command.commandId.equals( TradingProcessor.EXECUTE_SELL_ORDER ))
        {
            handler.executeSellOrder( command.portfolioId, command.symbol, command.quantity, command.price );
        }
        else
        {
            logger().log( String.format( "Unexpected command id '%d'", command.commandId ));
        }
    }

    protected void dispatchNotification( TradingNotification notification )
    {
        Vector<TradingProcessor> notificationInterestsVector = notificationInterests.get( notification.notificationId );
        for ( TradingProcessor notifier : notificationInterestsVector )
        {
            unwrapNotificationAndForward(notification, notifier);
            
            until.happened();
        }
    }

    protected void unwrapNotificationAndForward( TradingNotification notification, TradingProcessor notifier )
    {
        if ( notification.notificationId.equals( TradingProcessor.BUY_ORDER_EXECUTED ))
        {
            notifier.buyOrderExecuted( notification.portfolioId, notification.symbol, notification.quantity, notification.price );
        }
        else if ( notification.notificationId.equals( TradingProcessor.SELL_ORDER_EXECUTED ))
        {
            notifier.sellOrderExecuted( notification.portfolioId, notification.symbol, notification.quantity, notification.price );
        }
        else
        {
            logger().log( String.format( "Unexpected notification id '%s'", notification.notificationId ));
        }
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingBusProcessor#registerCommandHandler(java.lang.String, java.lang.String, io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor) */
    @Override
    public void registerHandler( RegisterCommandHandler register )
    {
        
        logger().log( String.format( "%s::registerCommandHandler( %s, %s, %s )", getClass().getSimpleName(), register.applicationId, register.commandId, register.handler.getClass().getSimpleName() ));
        CommandHandler commandHandler = new CommandHandler( register.commandId, register.applicationId, register.handler );
        Vector<TradingProcessor> commandHandlerVector = commandHandlers.get( register.commandId );
        if ( Objects.isNull( commandHandlerVector ))
        {
            commandHandlerVector = new Vector<>();
            commandHandlers.put( register.commandId, commandHandlerVector );
        }
        
        commandHandlerVector.add( commandHandler );
        
        until.happened();
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagebus.TradingBusProcessor#registerNotificationInterest(java.lang.String, java.lang.String, io.vlingo.reactive.messaging.patterns.messagebus.TradingProcessor) */
    @Override
    public void registerInterest( RegisterNotificationInterest register )
    {
        
        logger().log( String.format( "%s::registerNotificationInterest( %s, %s, %s )", getClass().getSimpleName(), register.applicationId, register.notificationId, register.getClass().getSimpleName() ));
        NotificationInterest notificationInterest = new NotificationInterest( register.notificationId, register.applicationId, register.interested );
        Vector<TradingProcessor> notificationInterestVector = notificationInterests.get( register.notificationId );
        if ( Objects.isNull( notificationInterestVector ))
        {
            notificationInterestVector = new Vector<>();
            notificationInterests.put( register.notificationId, notificationInterestVector );
        }
        
        notificationInterestVector.add( notificationInterest );
        
        until.happened();
    }

}
