/* Copyright (c) 2005-2018 - Blue River Systems Group, LLC - All Rights Reserved */
package io.vlingo.reactive.messaging.patterns.messagebus.exception;

/**
 * InvalidNotificationIdException
 *
 * <p>Copyright (c) 2005-2018 - Blue River Systems Group, LLC - All Rights Reserved</p>
 *
 * @author mas
 * @since Oct 31, 2018
 */
public class InvalidNotificationIdException 
extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public InvalidNotificationIdException( String message )
    {
        super( message );
    }
}
