// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * TradingNotification message that carries parameters related to a specific notification.
 *
 * @author brsg.io
 * @since Nov 2, 2018
 */
public class TradingNotification
{
    public final String notificationId;
    public final String portfolioId;
    public final String symbol;
    public final Integer quantity;
    public final Double price;
    
    public TradingNotification( String notificationId, String portfolioId, String symbol, Integer quantity, Double price )
    {
        this.notificationId = notificationId;
        this.portfolioId = portfolioId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }
}
