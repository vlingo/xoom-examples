// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.rest;

import io.vlingo.actors.World;
import io.vlingo.http.Filters;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application to run vlingo with a web server
 *
 * Pure demonstration of how to make a web server and getting it running inside 'world'
 */
class Bootstrap {
  private static Bootstrap bootstrap;
  /**
   * World MUST be hanging in the main() class static context so it is connected to root class loader
   * Other wise the application would terminate because thread returns from main()
   */
  private final World world;

  public static void main(String[] args) {
    bootstrap = new Bootstrap();
  }
  
  private Bootstrap() {
    System.out.println("==============================");
    System.out.println("Starting vlingo/http Server...");
    System.out.println("==============================");

    world = World.startWithDefaults("WebServerActorAwesomeDemo");

    final Servers servers=new Servers();

    servers.produceAdminServerAndAddToCache(world,9090);
    servers.produceOrganizationServerAndAddToCache(world,8081);


    //noinspection AnonymousHasLambdaAlternative
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        bootstrap.world.terminate();
        System.out.println("\n");
        System.out.println("==============================");
        System.out.println("Servers stopped by world ...");
        System.out.println("vlingo world terminated ...");
        System.out.println("==============================");
      }
    });
  }

  /**
   * Factory for servers and cache of these servers
   * for the demonstration of vlingo infrastructure - that web server is ONLY a an actor
   *
   * The demo handles multiple web servers to show the Hexagonal Architecture vlingo provides.
   */
  static class Servers {

    private final Map<Integer,Server> cache=new HashMap<>();
    /**
     * This server demonstrate the ability to open a port that only performs admin
     */
    private Server produceAdminServerAndAddToCache(World world, int port) {
      final AdminResource resource = new AdminResource(world,this);

      final Server server;
      server = Server.startWith(
              world.stage(),
              Resources.are(resource.routes()),
              Filters.none(),
              port,
              Sizing.defineWith(4, 10, 10, 1024),
              Timing.defineWith(3, 1, 100));
      cache.put(port,server);
      return server;
    }

    /**
     * Produces an busines server running on a given port.
     * In this demo, admin can make several parallel Actors just to emulate
     * Hexagonal Architecture (in real life making identical web-server actors make not much sense)
     *
     * @param port - port number must be different for each instance
     */

    Server produceOrganizationServerAndAddToCache(World world, int port) {
      final OrganizationResource resource = new OrganizationResource(world,port);

      final Server server;
      server = Server.startWith(
              world.stage(),
              Resources.are(resource.routes()),
              Filters.none(),
              port,
              Sizing.defineWith(4, 10, 10, 1024),
              Timing.defineWith(3, 1, 100));
      cache.put(port,server);
      return server;
    }

    public void stopServer(int port){
      Server server=cache.get(port);
      server.stop();
      cache.remove(port);
    }

    public String listPorts() {
      final List<Integer> collect;
      //noinspection SimplifyStreamApiCallChains
      collect = cache.keySet().stream().collect(Collectors.toList());
      return collect.toString();
    }
  }
}
