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
    private final String notificationId;
    private final String applicationId;
    private final TradingProcessor tradingProcessor;
    
    public NotificationInterest( String notificationId, String applicationId, TradingProcessor tradingProcessor )
    {
        this.notificationId = notificationId;
        this.applicationId = applicationId;
        this.tradingProcessor = tradingProcessor;
    }
    
    /**
     * Returns the value of {@link #notificationId}
     *
     * @return the value of {@link #notificationId}
     */
    public String getNotificationId()
    {
        return notificationId;
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
        result = prime * result + ((notificationId == null) ? 0 : notificationId.hashCode());
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
        NotificationInterest other = (NotificationInterest) obj;
        if (applicationId == null)
        {
            if (other.applicationId != null)
                return false;
        }
        else if (!applicationId.equals(other.applicationId))
            return false;
        if (notificationId == null)
        {
            if (other.notificationId != null)
                return false;
        }
        else if (!notificationId.equals(other.notificationId))
            return false;
        return true;
    }

}
