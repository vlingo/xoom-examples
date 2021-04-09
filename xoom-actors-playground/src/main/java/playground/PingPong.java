// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package playground;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.TestUntil;

public class PingPong {
  public static void main(final String[] args) throws Exception {
    final PingPong pingPong = PingPong.game();

    pingPong.play();
  }

  private final Pinger pinger;
  private final Ponger ponger;
  private final TestUntil until;
  private final World world;

  private static PingPong game() {
    final World world = World.startWithDefaults("ping-pong");
    final TestUntil until = TestUntil.happenings(1);
    final Pinger pinger = world.actorFor(Pinger.class, PingerActor.class, until);
    final Ponger ponger = world.actorFor(Ponger.class, PongerActor.class);

    return new PingPong(world, until, pinger, ponger);
  }

  private void play() {
    pinger.ping(ponger);

    until.completes();

    world.terminate();

    System.exit(0);
  }

  private PingPong(final World world, final TestUntil until, final Pinger pinger, final Ponger ponger) {
    this.world = world;
    this.until = until;
    this.pinger = pinger;
    this.ponger = ponger;
  }
}
