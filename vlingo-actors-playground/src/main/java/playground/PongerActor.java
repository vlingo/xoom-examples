// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package playground;

import io.vlingo.actors.Actor;

public class PongerActor extends Actor implements Ponger {
  private final Ponger self;

  public PongerActor() {
    self = selfAs(Ponger.class);
  }

  @Override
  public void pong(final Pinger pinger) {
    // Many times the ping-pong will complete before
    // the logger actor has an opportunity to process
    // and output all its log messages. Thus, the
    // System.out.println("pong") is used to ensure
    // visibility. If you'd like to see the logger
    // work you may uncomment the following line.
    //
    // logger().debug("pong");
    
    System.out.println("pong");
    
    pinger.ping(self);
  }
}
