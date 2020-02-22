// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.rest;

import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.delete;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

/**
 * Resource to play "admin" rest service
 * Demonstration of vlingo as actor infrastructure.
 *
 * The {@link io.vlingo.http.resource.Server} actor that handles web communication
 * this class fill in the resources - decides what URL's to expose + and
 * handles the calls from clients.
 *
 */
class AdminResource {
  private final World world;
  private final Bootstrap.Servers servers;
  public AdminResource(final World world, Bootstrap.Servers servers) {
    this.world = world;
    this.servers=servers;
  }

  /**
   * Demonstrate that terminate the world will stop the actors - also the web server.
   * @return {@code Completes<Response>}
   */
  public Completes<Response> terminateWorld() {
    world.terminate();
    return Completes.withSuccess(Response.of(Ok, "the server is stopping\n"));
  }

  /**
   * Demonstrate that we can make a new server.
   * @return {@code Completes<Response>}
   */
  public Completes<Response> open(Integer port) {
    servers.produceOrganizationServerAndAddToCache(world,port);
    return Completes.withSuccess(Response.of(Ok, String.format("Opening a web server on port %s\n",port)));
  }
  /**
   * Demonstrate that we can stop a server.
   * @return {@code Completes<Response>}
   */
  public Completes<Response> close(Integer port) {
 //   Bootstrap.bootstrap.server.stop();
    servers.stopServer(port);
    return Completes.withSuccess(Response.of(Ok, String.format("Server on port %s stopping\n",port)));
  }
  /**
   * Demonstrate that we can garbage collect
   * @return {@code Completes<Response>}
   */
  public Completes<Response> gc() {

    return Completes.withSuccess(Response.of(Ok, String.format("Performed GC %s\n","gc")));
  }

  /**
   * Gets information about what is running
   * @return {@code Completes<Response>}
   */
  public Completes<Response> serverInfo() {
    //noinspection StringBufferReplaceableByString
    StringBuilder sb=new StringBuilder();
//    sb.append(String.format("thread=%s",Thread.currentThread().toString()));
    sb.append(String.format("ports=%s",servers.listPorts()));
    sb.append("\n");
    return Completes.withSuccess(Response.of(Ok, sb.toString()));
  }

  /**
   * Answer the {@code Resource} of REST method handlers.
   * @return {@code Resource<?>}
   */
  public Resource<?> routes() {
    return resource("Admin services Resource API",
            get("/admin")
                    .handle(this::serverInfo),
            delete("/admin")
                    .handle(this::terminateWorld),
            post("/admin/gc")
                    .handle(this::gc),
            post("/admin/server/{port}")
                    .param(Integer.class)
                    .handle(this::open),
            delete("/admin/server/{port}")
                    .param(Integer.class)
                    .handle(this::close)
    );
  }

}