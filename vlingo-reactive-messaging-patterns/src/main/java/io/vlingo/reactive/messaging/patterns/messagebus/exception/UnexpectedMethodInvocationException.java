// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagebus.exception;

/**
 * UnexpectedMethodInvocationException
 *
 * @author brsg.io
 * @since Oct 31, 2018
 */
public class UnexpectedMethodInvocationException 
extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public UnexpectedMethodInvocationException( String message )
    {
        super( message );
    }

}
