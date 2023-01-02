// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.returnaddress;

import org.junit.Assert;
import org.junit.Test;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;

public class ReturnAddressTest {
  @Test
  public void testThatReturnAddressRuns() {
    System.out.println("ReturnAddress: is starting.");

    final World world = World.startWithDefaults("returnaddress-test");

    final ReturnAddressResults results = new ReturnAddressResults();
    final AccessSafely access = results.afterCompleting(2);

    final Service service = world.actorFor(Service.class, Server.class);
    world.actorFor(Consumer.class, Client.class, service, results);

    Assert.assertEquals(1, (int) access.readFrom("afterSimpleReplyCount"));
    Assert.assertEquals(1, (int) access.readFrom("afterComplexReplyCount"));

    System.out.println("ReturnAddress: is completed.");
  }
}
