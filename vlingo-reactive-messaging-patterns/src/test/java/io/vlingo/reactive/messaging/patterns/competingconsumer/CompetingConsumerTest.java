// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.competingconsumer;
/**
 * CompetingConsumerTest provides an example of implementing
 * and using a vlingo Router.
 */
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

public class CompetingConsumerTest {

  @Test
  public void testThatConsumersCompete() {
    final World world = World.startWithDefaults("competing-consumer-test");
    final int poolSize = 4;
    final int messagesToSend = 8;
    final TestUntil until = TestUntil.happenings(messagesToSend);
    final WorkConsumer workConsumer = world.actorFor(
            Definition.has(WorkRouterActor.class, Definition.parameters(poolSize, until)),
            WorkConsumer.class);
    
    for (int i = 0; i < messagesToSend; i++) {
      workConsumer.consumeWork(new WorkItem(i));
    }
    
    until.completes();
  }
}
