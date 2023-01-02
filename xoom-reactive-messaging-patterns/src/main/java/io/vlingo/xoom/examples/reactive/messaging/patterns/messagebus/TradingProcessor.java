// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus;

/**
 * TradingProcessor defines methods that brings together disparate systems
 * in this case covering trades as commands, management and analysis as notifications
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
    
}
