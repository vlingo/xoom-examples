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

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

/**
 * PeerNodeActor represents point-to-point messaging that underlies all {@link Actor} communication where
 * messages between two peers will be received in the order that they are sent.
 *
 * @author brsg.io
 * @since Oct 26, 2018
 */
public class PeerNodeActor 
extends Actor
implements PointToPointProcessor
{
    private final TestUntil testUntil;
    private int lastOrderedMessageId = 0;
    
    public PeerNodeActor( TestUntil testUntil )
    {
        this.testUntil = testUntil;
    }

    /* @see io.vlingo.reactive.messaging.patterns.pointtopointchannel.PointToPointProcessor#peerMessage(java.lang.String) */
    @Override
    public void process( Integer messageId )
    {
        logger().log( String.format( "peerMessage %d received", messageId ));
        if ( messageId < lastOrderedMessageId ) throw new IllegalStateException( "Message id out of order" );
        lastOrderedMessageId = messageId;
        testUntil.happened();
    }

}
