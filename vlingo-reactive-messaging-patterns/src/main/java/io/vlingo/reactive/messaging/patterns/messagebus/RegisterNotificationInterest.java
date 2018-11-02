// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * RegisterNotificationInterest
 *
 * @author brsg.io
 * @since Nov 2, 2018
 */
public class RegisterNotificationInterest
{
    public final String applicationId;
    public final String notificationId;
    public final TradingProcessor interested;
    
    public RegisterNotificationInterest( String applicationId, String notificationId, TradingProcessor interested )
    {
        this.applicationId = applicationId;
        this.notificationId = notificationId;
        this.interested = interested;
    }
}
