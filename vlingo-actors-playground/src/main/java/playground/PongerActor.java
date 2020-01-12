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
    logger().debug("pong");
    pinger.ping(self);
  }
}
