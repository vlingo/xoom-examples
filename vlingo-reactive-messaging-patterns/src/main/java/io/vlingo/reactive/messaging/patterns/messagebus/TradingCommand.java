// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * TradingCommand
 *
 * @author brsg.io
 * @since Nov 2, 2018
 */
public class TradingCommand
{
    public final String commandId;
    public final String portfolioId;
    public final String symbol;
    public final Integer quantity;
    public final Double price;
    
    public TradingCommand( String commandId, String portfolioId, String symbol, Integer quantity, Double price )
    {
        this.commandId = commandId;
        this.portfolioId = portfolioId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }
}
