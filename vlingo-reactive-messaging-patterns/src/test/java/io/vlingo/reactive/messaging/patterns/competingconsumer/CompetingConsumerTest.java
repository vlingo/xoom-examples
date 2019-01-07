// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.competingconsumer;
/**
 * CompetingConsumerTest demonstrates the competing consumers
 * pattern utilizing a {@link Router} with a {@link RoundRobinRoutingStrategy}.
 * In this example, a {@link WorkRouterActor} routes instances of
 * a simple {@link WorkItem} object to a pool of {@link WorkConsumerActor},
 * any of which might be asked to consume the {@link WorkItem} at the
 * determination of the {@link Router}.
 */
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

public class CompetingConsumerTest {

  @Test
  public void testThatConsumersCompete() {
    final World world = World.startWithDefaults("competing-consumer-test");
    final int poolSize = 4;
    final int messagesToSend = 8;
    final TestUntil until = TestUntil.happenings(messagesToSend);
    final WorkConsumer workConsumer =
            world.actorFor(WorkConsumer.class,  WorkRouterActor.class, poolSize, until);
    
    for (int i = 0; i < messagesToSend; i++) {
      workConsumer.consumeWork(new WorkItem(i));
    }
    
    until.completes();
  }
}
