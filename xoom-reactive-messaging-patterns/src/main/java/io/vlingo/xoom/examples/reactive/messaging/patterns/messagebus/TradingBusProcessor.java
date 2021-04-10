// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus;

import io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.TradingBusCommands.RegisterCommandHandler;
import io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.TradingBusCommands.RegisterNotificationInterest;
import io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.TradingBusCommands.TradingCommand;
import io.vlingo.xoom.examples.reactive.messaging.patterns.messagebus.TradingBusCommands.TradingNotification;

/**
 * TradingBusProcessor defines the public interface for this {@link TradingBus} for handling
 * registration, trade, and notify messages.
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
