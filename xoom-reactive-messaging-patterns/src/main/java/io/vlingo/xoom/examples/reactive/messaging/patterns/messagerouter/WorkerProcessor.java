// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.messagerouter;

import io.vlingo.xoom.actors.Actor;

/**
 * WorkerProcessor {@link Actor} able to perform workloads based on {@link Processor} interface.  
 * The route method is the hook for where this work is to be performed. 
 */
public class WorkerProcessor
extends Actor
implements Processor
{
    public static final int EXPECTED_DIFFERENCE = 2;
    private final String name;
    private int localCount = 0;
    
    public WorkerProcessor( String name )
    {
        this.name = name;
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.messagerouter.Processor#route() */
    @Override
    public void process( Integer count )
    {
        validateProcess(count);
    }

    protected void validateProcess(Integer count)
    {
        if ( localCount != 0 ) 
        {
            validateCount(count);
        }
        
        localCount = count;
    }

    protected void validateCount(Integer count)
    {
        int difference = count - localCount;
        if ( difference != EXPECTED_DIFFERENCE )
        {
            throw new IllegalStateException( 
                String.format( 
                    "Expected sequential, alternating count incrementing by %d but was %d for %s", 
                    EXPECTED_DIFFERENCE, difference, toString() 
                )
            );
        }
        else
        {
            logger().debug(
                String.format( "As expected %d::%d::%d::%s", localCount, count, difference, toString() ) 
            );
        }
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

    /* @see io.vlingo.xoom.actors.Actor#toString() */
    @Override
    public String toString()
    {
        return String.format( "WorkerProcessor: %s", getName() );
    }

}
