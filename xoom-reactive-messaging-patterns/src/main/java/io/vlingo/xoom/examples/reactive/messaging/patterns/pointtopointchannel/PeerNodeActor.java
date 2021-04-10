// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.pointtopointchannel;

import io.vlingo.xoom.actors.Actor;

/**
 * PeerNodeActor represents point-to-point messaging that underlies all {@link Actor} communication where
 * messages between two peers will be received in the order that they are sent.
 */
public class PeerNodeActor 
extends Actor
implements PointToPointProcessor
{
    private final PointToPointResults results;
    private int lastOrderedMessageId = 0;
    
    public PeerNodeActor( final PointToPointResults results )
    {
        this.results = results;
    }

    /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.pointtopointchannel.PointToPointProcessor#peerMessage(java.lang.String) */
    @Override
    public void process( Integer messageId )
    {
        logger().debug( String.format( "peerMessage %d received", messageId ));
        if ( messageId < lastOrderedMessageId ) throw new IllegalStateException( "Message id out of order" );
        lastOrderedMessageId = messageId;
        results.access.writeUsing("afterMessageProcessedCount", 1);
    }

}
