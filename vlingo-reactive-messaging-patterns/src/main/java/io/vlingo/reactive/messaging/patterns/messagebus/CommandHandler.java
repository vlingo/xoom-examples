// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus;

/**
 * CommandHandler
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class CommandHandler
{
    private final String commandId;
    private final String applicationId;
    private final TradingProcessor tradingProcessor;
    
    public CommandHandler( String commandId, String applicationId, TradingProcessor tradingProcessor )
    {
        this.commandId = commandId;
        this.applicationId = applicationId;
        this.tradingProcessor = tradingProcessor;
    }

    /**
     * Returns the value of {@link #commandId}
     *
     * @return the value of {@link #commandId}
     */
    public String getCommandId()
    {
        return commandId;
    }

    /**
     * Returns the value of {@link #applicationId}
     *
     * @return the value of {@link #applicationId}
     */
    public String getApplicationId()
    {
        return applicationId;
    }

    /**
     * Returns the value of {@link #tradingProcessor}
     *
     * @return the value of {@link #tradingProcessor}
     */
    public TradingProcessor getTradingProcessor()
    {
        return tradingProcessor;
    }

    /* @see java.lang.Object#hashCode() */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
        result = prime * result + ((commandId == null) ? 0 : commandId.hashCode());
        return result;
    }

    /* @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommandHandler other = (CommandHandler) obj;
        if (applicationId == null)
        {
            if (other.applicationId != null)
                return false;
        }
        else if (!applicationId.equals(other.applicationId))
            return false;
        if (commandId == null)
        {
            if (other.commandId != null)
                return false;
        }
        else if (!commandId.equals(other.commandId))
            return false;
        return true;
    }

}
