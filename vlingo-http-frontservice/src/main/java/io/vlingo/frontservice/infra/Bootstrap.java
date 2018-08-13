// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra;

import io.vlingo.actors.World;
import io.vlingo.http.resource.Server;

public class Bootstrap {
  private static final Bootstrap bootStrap = new Bootstrap();

  private final Server server;
  private final World world;

  public static void main(final String[] args) throws Exception {
    System.out.println("=======================");
    System.out.println("frontservice: started.");
    System.out.println("=======================");
  }

  private Bootstrap() {
    this.world = World.startWithDefaults("frontservice");
    this.server = Server.startWith(world.stage());

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        bootStrap.server.stop();
        System.out.println("\n");
        System.out.println("=======================");
        System.out.println("Stopping frontservice.");
        System.out.println("=======================");
        pause();
      }
    });
  }

  private void pause() {
    try {
      Thread.sleep(1000L);
    } catch (Exception e) {
      // ignore
    }
  }
}
