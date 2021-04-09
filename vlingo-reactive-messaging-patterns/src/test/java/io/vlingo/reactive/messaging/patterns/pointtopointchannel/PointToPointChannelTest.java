// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.pointtopointchannel;

import org.junit.Assert;
import org.junit.Test;

import io.vlingo.actors.Actor;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;

/**
 * PointToPointChannelTest demonstrates that {@link Actor} communication is naturally point-to-point
 * and, therefore, the messages from point A to point B are sent and received sequentially. 
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
        
        world.defaultLogger().debug( "PointToPointChannel: is starting" );
        
        final PointToPointResults results = new PointToPointResults();

        final AccessSafely access = results.afterCompleting( NUMBER_MESSAGES );
        
        final PointToPointProcessor peerNodeActor = world.actorFor(PointToPointProcessor.class, PeerNodeActor.class, results);
        
        peerNodeActor.process( MSG_ID_1 );
        peerNodeActor.process( MSG_ID_2 );
        peerNodeActor.process( MSG_ID_3 );
        peerNodeActor.process( MSG_ID_4 );

        Assert.assertEquals(NUMBER_MESSAGES.intValue(), (int) access.readFrom("afterMessageProcessedCount"));
        
        world.defaultLogger().debug( "PointToPointChannel: is completed" );
        
        world.terminate();
        
    }

}
