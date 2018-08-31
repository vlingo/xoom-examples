// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package playground;

import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

public class PlaygroundTest {

  @Test
  public void testPlayPingPong() {
    final World world = World.startWithDefaults("playground");
    final TestUntil until = TestUntil.happenings(1);
    final Pinger pinger = world.actorFor(Definition.has(PingerActor.class, Definition.parameters(until)), Pinger.class);
    final Ponger ponger = world.actorFor(Definition.has(PongerActor.class, Definition.NoParameters), Ponger.class);

    pinger.ping(ponger);

    until.completes();

    world.terminate();
  }
}
