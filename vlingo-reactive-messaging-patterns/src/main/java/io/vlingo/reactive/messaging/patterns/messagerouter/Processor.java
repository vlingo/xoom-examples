// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagerouter;

import io.vlingo.actors.Actor;

/**
 * Processor {@link Actor} able to perform workloads based on {@link RoutableMessage} interface.  
 * The route method is the hook for where this work is to be performed. 
 *
 * @author brsg.io
 * @since Oct 25, 2018
 */
public class Processor
extends Actor
implements RoutableMessage
{
    private final String name;
    
    public Processor( String name )
    {
        this.name = name;
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagerouter.RoutableMessage#route() */
    @Override
    public void route()
    {
        logger().log( String.format( "%s::route", toString() )); 
    }

    /**
     * Returns the value of {@link #name}
     *
     * @return the value of {@link #name}
     */
    public String getName()
    {
        return name;
    }

    /* @see io.vlingo.actors.Actor#toString() */
    @Override
    public String toString()
    {
        return String.format( "Processor: %s", getName() );
    }

}
