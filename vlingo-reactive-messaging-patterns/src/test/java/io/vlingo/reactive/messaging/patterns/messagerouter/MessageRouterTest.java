// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagerouter;

import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

/**
 * MessageRouterTest serves as simple driver for @l{@link AlternatingRouter} @ {@link Actor} delegation
 * of workload to a pool of {@link RoutableMessage} processors based on simple alternating strategy.  The
 * route-able message pattern can take on evermore sophisticated strategies extrapolated from this example.
 *
 * @author brsg.io
 * @since Oct 25, 2018
 */
public class MessageRouterTest
{
    public static final String WORLD_NAME = "alternating-message-router";

    @Test
    public void testAlternatingRouterRuns()
    throws Exception
    {
        System.out.println( "AlternatingRouter: is starting"  );
        
        final World world = World.startWithDefaults( WORLD_NAME );
        
        final TestUntil until = TestUntil.happenings( 20 );
        
        final RoutableMessage messageProcessor1 = world.actorFor( Definition.has( Processor.class, Definition.parameters( "MP One" ) ), RoutableMessage.class );
        final RoutableMessage messageProcessor2 = world.actorFor( Definition.has( Processor.class, Definition.parameters( "MP Two" )), RoutableMessage.class );
        final RoutableMessage alternatingRouter = world.actorFor( Definition.has( AlternatingRouter.class, Definition.parameters( until, messageProcessor1, messageProcessor2 )), RoutableMessage.class );
        
        int i = 1;
        while ( until.remaining() > 0 )
        {
            alternatingRouter.route();
            System.out.println( String.format( "Count: %d", i ));
            i++;
            Thread.sleep( 1 );
        }
        
        until.completes();
        world.terminate();
        
        System.out.println( "AlternatingRouter: is completed"  );
    }

}
