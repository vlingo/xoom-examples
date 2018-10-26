// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.messagerouter;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

/**
 * AlternatingRouter {@link Actor} that delegates workload to {@link RoutableMessage} {@link Actor} based
 * on simple alternating algorithm.  This could be used to extrapolate to round-robin and other
 * more sophisticated delegation strategies.
 *
 * @author brsg.io
 * @since Oct 25, 2018
 */
public class AlternatingRouter 
extends Actor 
implements RoutableMessage
{
    private TestUntil testUntil;
    private final RoutableMessage processor1;
    private final RoutableMessage processor2;
    private int alternate = 1;
    
    public AlternatingRouter( TestUntil testUntil, RoutableMessage processor1, RoutableMessage processor2 )
    {
        this.testUntil = testUntil;
        this.processor1 = processor1;
        this.processor2 = processor2;
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagerouter.RoutableMessage#route() */
    @Override
    public void route()
    {
        if ( alternate == 1 )
        {
            alternate = 2;
            processor1.route();
        }
        else 
        {
            alternate = 1;
            processor2.route();
        }
        
        testUntil = testUntil.happened();
    }

}
