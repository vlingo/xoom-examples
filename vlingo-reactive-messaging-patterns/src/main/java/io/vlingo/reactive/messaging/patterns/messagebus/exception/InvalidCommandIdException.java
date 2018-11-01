/* Copyright (c) 2005-2018 - Blue River Systems Group, LLC - All Rights Reserved */
package io.vlingo.reactive.messaging.patterns.messagebus.exception;

/**
 * InvalidCommandIdException
 *
 * <p>Copyright (c) 2005-2018 - Blue River Systems Group, LLC - All Rights Reserved</p>
 *
 * @author mas
 * @since Oct 31, 2018
 */
public class InvalidCommandIdException 
extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public InvalidCommandIdException( String message )
    {
        super( message );
    }
}
