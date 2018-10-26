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
 * AlternatingRouteProcessor {@link Actor} that delegates workload to {@link Processor} {@link Actor} based
 * on simple alternating algorithm.  This could be used to extrapolate to round-robin and other
 * more sophisticated delegation strategies.
 *
 * @author brsg.io
 * @since Oct 25, 2018
 */
public class AlternatingRouteProcessor 
extends Actor 
implements Processor
{
    private TestUntil testUntil;
    private final Processor processor1;
    private final Processor processor2;
    private int alternate = 1;
    
    public AlternatingRouteProcessor( TestUntil testUntil, Processor processor1, Processor processor2 )
    {
        this.testUntil = testUntil;
        this.processor1 = processor1;
        this.processor2 = processor2;
    }

    /* @see io.vlingo.reactive.messaging.patterns.messagerouter.Processor#route() */
    @Override
    public void process()
    {
        if ( alternate == 1 )
        {
            alternate = 2;
            processor1.process();
        }
        else 
        {
            alternate = 1;
            processor2.process();
        }
        
        testUntil = testUntil.happened();
    }

}
