// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.pointtopointchannel;

import org.junit.Test;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

/**
 * PointToPointChannelTest demonstrates that {@link Actor} communication is naturally point-to-point
 * and, therefore, the messages from point A to point B are sent and received sequentially. 
 *
 * @author brsg.io
 * @since Oct 26, 2018
 */
public class PointToPointChannelTest
{

    public static final String WORLD_NAME = "point-to-point-channel-example";
    public static final Integer MSG_ID_1 = 1;
    public static final Integer MSG_ID_2 = 2;
    public static final Integer MSG_ID_3 = 3;
    public static final Integer MSG_ID_4 = 4;
    public static final Integer NUMBER_MESSAGES = 4;

    @Test
    public void testPointToPointChannelRuns()
    {
        final World world = World.startWithDefaults( WORLD_NAME );
        
        world.defaultLogger().log( "PointToPointChannel: is starting" );
        
        final TestUntil until = TestUntil.happenings( NUMBER_MESSAGES );
        
        final PointToPointProcessor peerNodeActor = world.actorFor( Definition.has( PeerNodeActor.class, Definition.parameters( until )), PointToPointProcessor.class );
        
        peerNodeActor.process( MSG_ID_1 );
        peerNodeActor.process( MSG_ID_2 );
        peerNodeActor.process( MSG_ID_3 );
        peerNodeActor.process( MSG_ID_4 );
        
        until.completes();
        
        world.defaultLogger().log( "PointToPointChannel: is completed" );
        
        world.terminate();
        
    }

}
