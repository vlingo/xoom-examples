// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagerouter;

import org.junit.Test;

import io.vlingo.actors.Actor;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

/**
 * MessageRouterTest serves as simple driver for {@link AlternatingRouteProcessor} {@link Actor} delegation
 * of workload to a pool of {@link Processor} processors based on simple alternating strategy.  The
 * route-able message pattern can take on evermore sophisticated strategies extrapolated from this example.
 */
public class MessageRouterTest
{
    public static final String WORLD_NAME = "alternating-message-router";
    public static final int ROUTES = 20;

    @Test
    public void testAlternatingRouteProcessorRuns()
    throws Exception
    {
        
        final World world = World.startWithDefaults( WORLD_NAME );
        
        world.defaultLogger().log( "AlternatingRouteProcessor: is starting"  );
        
        final TestUntil until = TestUntil.happenings( ROUTES );
        
        final Processor messageProcessor1 = world.actorFor(Processor.class, WorkerProcessor.class, "MP One" );
        final Processor messageProcessor2 = world.actorFor(Processor.class, WorkerProcessor.class, "MP Two" );
        final Processor alternatingRouter = world.actorFor(Processor.class, AlternatingRouteProcessor.class, until, messageProcessor1, messageProcessor2 );
        
        int routeCount = 0;
        int j = 0;
        while ( true )
        {
            int remaining = until.remaining();
            if ( routeCount < ROUTES )
            {
                alternatingRouter.process( routeCount );
                routeCount++;
            }
            
            if ( j != remaining )
            {
                world.defaultLogger().log( String.format( "Count: %d", remaining ));
                j = remaining;
            }
            
            if ( remaining == 0 ) break;
        }
        
        until.completes();
        
        world.defaultLogger().log( "AlternatingRouteProcessor: is completed"  );
        
        world.terminate();
        
    }

}
