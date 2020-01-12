// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * TradingBusCommands
 */
public class TradingBusCommands
{
    public static final class RegisterCommandHandler
    {
        public final String applicationId;
        public final String commandId;
        public final TradingProcessor handler;
        
        public RegisterCommandHandler( final String applicationId, final String commandId, final TradingProcessor handler )
        {
            this.applicationId = applicationId;
            this.commandId = commandId;
            this.handler = handler;
        }
    }

    public static final class RegisterNotificationInterest
    {
        public final String applicationId;
        public final String notificationId;
        public final TradingProcessor interested;
        
        public RegisterNotificationInterest( final String applicationId, final String notificationId, final TradingProcessor interested )
        {
            this.applicationId = applicationId;
            this.notificationId = notificationId;
            this.interested = interested;
        }
    }

    public static final class TradingCommand
    {
        public final String commandId;
        public final String portfolioId;
        public final String symbol;
        public final Integer quantity;
        public final Double price;
        
        public TradingCommand( final String commandId, final String portfolioId, final String symbol, final Integer quantity, final Double price )
        {
            this.commandId = commandId;
            this.portfolioId = portfolioId;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
        }
    }

    public static final class TradingNotification
    {
        public final String notificationId;
        public final String portfolioId;
        public final String symbol;
        public final Integer quantity;
        public final Double price;
        
        public TradingNotification( final String notificationId, final String portfolioId, final String symbol, final Integer quantity, final Double price )
        {
            this.notificationId = notificationId;
            this.portfolioId = portfolioId;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
        }
    }
}
