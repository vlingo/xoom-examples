// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * RegisterCommandHandler
 *
 * @author brsg.io
 * @since Nov 2, 2018
 */
public class RegisterCommandHandler
{
    public final String applicationId;
    public final String commandId;
    public final TradingProcessor handler;
    
    public RegisterCommandHandler( String applicationId, String commandId, TradingProcessor handler )
    {
        this.applicationId = applicationId;
        this.commandId = commandId;
        this.handler = handler;
    }
}
