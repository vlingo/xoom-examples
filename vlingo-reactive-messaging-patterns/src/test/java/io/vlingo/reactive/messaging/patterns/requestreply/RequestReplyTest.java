// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.requestreply;

import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

public class RequestReplyTest {
  @Test
  public void testThatRequestReplyRuns() {
    System.out.println("RequestReply: is starting.");

    final World world = World.startWithDefaults("requestreply-test");

    final TestUntil until = TestUntil.happenings(1);

    final Service service = world.actorFor(Definition.has(Server.class, Definition.NoParameters), Service.class);
    world.actorFor(Definition.has(Client.class, Definition.parameters(service, until)), Consumer.class);

    until.completes();

    System.out.println("RequestReply: is completed.");
  }
}
