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
    
}
