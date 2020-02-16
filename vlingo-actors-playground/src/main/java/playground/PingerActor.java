// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package playground;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

public class PingerActor extends Actor implements Pinger {
  private int count;
  private final Pinger self;
  private final TestUntil until;

  public PingerActor(final TestUntil until) {
    this.until = until;
    this.count = 0;
    this.self = selfAs(Pinger.class);
  }

  @Override
  public void ping(final Ponger ponger) {
    ++count;
    // Many times the ping-pong will complete before
    // the logger actor has an opportunity to process
    // and output all its log messages. Thus, the
    // System.out.println("pong") is used to ensure
    // visibility. If you'd like to see the logger
    // work you may uncomment the following line.
    //
    // logger().debug("ping " + count);
    
    System.out.println("ping");
    
    if (count >= 10) {
      self.stop();
      ponger.stop();
    } else {
      ponger.pong(self);
    }
  }

  @Override
  protected void afterStop() {
    logger().debug("Pinger " + address() + " just stopped!");
    until.happened();
    super.afterStop();
  }
}
