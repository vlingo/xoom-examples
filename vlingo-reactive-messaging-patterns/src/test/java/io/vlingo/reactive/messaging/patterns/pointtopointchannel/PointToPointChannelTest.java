//   Copyright 2012,2015 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
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
