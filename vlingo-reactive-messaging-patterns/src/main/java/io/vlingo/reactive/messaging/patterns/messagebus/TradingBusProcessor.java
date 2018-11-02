// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * TradingBusProcessor
 *
 * @author brsg.io
 * @since Nov 2, 2018
 */
public interface TradingBusProcessor
{
    // REGISTRATION
    void registerHandler( RegisterCommandHandler registerHandler );
    void registerInterest( RegisterNotificationInterest registerInterest );
    
    // ACTION
    void trade( TradingCommand command );
    void notify( TradingNotification notification );
}
