// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.infra;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.examples.frontservice.infra.persistence.CommandModelStoreProvider;
import io.vlingo.xoom.examples.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.xoom.examples.frontservice.infra.projection.ProjectionDispatcherProvider;
import io.vlingo.xoom.examples.frontservice.resource.ProfileResource;
import io.vlingo.xoom.examples.frontservice.resource.UserResource;
import io.vlingo.xoom.http.resource.Configuration.Sizing;
import io.vlingo.xoom.http.resource.Configuration.Timing;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.Server;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;

public class Bootstrap {
  private static Bootstrap instance;
  private final StatefulTypeRegistry registry;
  public final Server server;

  //  public static final Bootstrap testInstance() {
//    if (instance == null) instance = new Bootstrap(true);
//    return instance;
//  }
  public final World world;

  private Bootstrap() {
    this.world = World.startWithDefaults("frontservice");

    registry = new StatefulTypeRegistry(world);

    QueryModelStoreProvider.using(world.stage(), registry);
    CommandModelStoreProvider.using(world.stage(), registry, ProjectionDispatcherProvider.using(world.stage()).storeDispatcher);

    final UserResource userResource = new UserResource(world);
    final ProfileResource profileResource = new ProfileResource(world);

    final Resources resources = Resources.are(userResource.routes(), profileResource.routes());

    this.server = Server.startWith(world.stage(), resources, 8081, Sizing.define(), Timing.define());

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

  public static final Bootstrap instance() {
    if (instance == null) instance = new Bootstrap();
    return instance;
  }

  public static final void terminateTestInstance() {
    instance = null;
  }

  public static void main(final String[] args) throws Exception {
    System.out.println("=======================");
    System.out.println("frontservice: started.");
    System.out.println("=======================");
    Bootstrap.instance();
  }

  private void pause() {
    try {
      Thread.sleep(1000L);
    } catch (Exception e) {
      // ignore
    }
  }
}
