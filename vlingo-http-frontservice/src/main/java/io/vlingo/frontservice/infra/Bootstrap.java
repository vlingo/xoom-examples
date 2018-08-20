// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra;

import io.vlingo.actors.World;
import io.vlingo.frontservice.infra.persistence.CommandModelStoreProvider;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.infra.projection.ProjectionDispatcherProvider;
import io.vlingo.http.resource.Server;

public class Bootstrap {
  private static Bootstrap instance;

  public static final Bootstrap instance() {
    if (instance == null) instance = new Bootstrap();
    return instance;
  }

//  public static final Bootstrap testInstance() {
//    if (instance == null) instance = new Bootstrap(true);
//    return instance;
//  }

  public static final void terminateTestInstance() {
    instance = null;
  }

  public final Server server;
  public final World world;

  public static void main(final String[] args) throws Exception {
    System.out.println("=======================");
    System.out.println("frontservice: started.");
    System.out.println("=======================");
    Bootstrap.instance();
  }

  private Bootstrap() {
    this.world = World.startWithDefaults("frontservice");

    QueryModelStoreProvider.using(world.stage());

    CommandModelStoreProvider.using(world.stage(), ProjectionDispatcherProvider.using(world.stage()).textStateStoreDispatcher);

    this.server = Server.startWith(world.stage());

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        if (instance != null) {
          instance.server.stop();

          System.out.println("\n");
          System.out.println("=======================");
          System.out.println("Stopping frontservice.");
          System.out.println("=======================");
          pause();
        }
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
