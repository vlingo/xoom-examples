// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.rest;

import io.vlingo.actors.World;
import io.vlingo.http.Filters;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;

public class Bootstrap {
  private static Bootstrap bootstrap;
  
  private final OrganizationResource resource;
  private final Server server;
  private final World world;

  public static void main(String[] args) {
    bootstrap = new Bootstrap();
  }
  
  private Bootstrap() {
    System.out.println("==============================");
    System.out.println("Starting vlingo/http Server...");
    System.out.println("==============================");
    
    world = World.startWithDefaults("InspectionsResourceTest");
    
    resource = new OrganizationResource(world);
    
    server =
            Server.startWith(
                    world.stage(),
                    Resources.are(resource.routes()),
                    Filters.none(),
                    8081,
                    Sizing.defineWith(4, 10, 10, 1024),
                    Timing.defineWith(3, 1, 100));

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        if (bootstrap != null) {
          bootstrap.server.stop();

          System.out.println("\n");
          System.out.println("==============================");
          System.out.println("Stopping vlingo/http Server...");
          System.out.println("==============================");
        }
      }
    });
  }
}
