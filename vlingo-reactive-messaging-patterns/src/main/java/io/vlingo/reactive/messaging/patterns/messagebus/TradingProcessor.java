// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * TradingProcessor is a representation of a Canonical Message Model that brings together disparate systems
 * by representing methods that span all three systems, in this case covering trades as commands,
 * management and analysis as notifications, and the bus' configuration through registration.
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public interface TradingProcessor
{
    // CONSTANTS - COMMANDS
    public static final String EXECUTE_BUY_ORDER = "executeBuyOrder";
    public static final String EXECUTE_SELL_ORDER = "executeSellOrder";
    // CONSTANTS - NOTIFICATIONS
    public static final String BUY_ORDER_EXECUTED = "buyOrderExecuted";
    public static final String SELL_ORDER_EXECUTED = "sellOrderExecuted";
    
    // COMMANDS
    void executeBuyOrder( String portfolioId, String symbol, Integer quantity, Double price );
    void executeSellOrder( String portfolioId, String symbol, Integer quantity, Double price );
    
    // NOTIFICATION
    void buyOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price );
    void sellOrderExecuted( String portfolioId, String symbol, Integer quantity, Double price );
    
    // REGISTRATION
    void registerCommandHandler( String applicationId, String commandId, TradingProcessor handler );
    void registerNotificationInterest( String applicationId, String notificationId, TradingProcessor interested );
}
