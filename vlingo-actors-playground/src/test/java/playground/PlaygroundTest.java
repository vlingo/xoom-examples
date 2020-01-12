// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package playground;

import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

public class PlaygroundTest {

  @Test
  public void testPlayPingPong() {
    final World world = World.startWithDefaults("playground");
    final TestUntil until = TestUntil.happenings(1);
    final Pinger pinger = world.actorFor(Pinger.class, PingerActor.class, until);
    final Ponger ponger = world.actorFor(Ponger.class, PongerActor.class);

    pinger.ping(ponger);

    until.completes();

    world.terminate();
  }
}
