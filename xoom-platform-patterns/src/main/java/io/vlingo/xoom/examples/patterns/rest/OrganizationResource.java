// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.rest;

import static io.vlingo.xoom.http.Response.Status.Created;
import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.ResponseHeader.headers;
import static io.vlingo.xoom.http.ResponseHeader.of;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;
import static io.vlingo.xoom.http.resource.ResourceBuilder.patch;
import static io.vlingo.xoom.http.resource.ResourceBuilder.post;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

import java.util.UUID;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.Resource;

/**
 * Example of VLINGO XOOM Http Fluent API mappings.
 */
class OrganizationResource {
  private final World world;
  private final int port;

  OrganizationResource(final World world, int port) {
    this.world = world;
    this.port = port;
  }

  OrganizationResource(final World world) {
    this(world,-1);
  }


  /**
   * Answer the eventual {@code Response} of defining a new Organization.
   * @return {@code Completes<Response>}
   */
  public Completes<Response> info() {

    world.defaultLogger().debug(getClass().getSimpleName() + ": info(): " + port);

    return Completes.withSuccess(Response.of(Ok, String.format("port=%s\n",port)));
  }

  /**
   * Answer the eventual {@code Response} to the GET query of a given Organization.
   * @param OrganizationId the String id of the Organization to query
   * @return {@code Completes<Response>}
   */
  public Completes<Response> queryOrganization(final String OrganizationId) {
    world.defaultLogger().debug(getClass().getSimpleName() + ": queryOrganization(\"" + OrganizationId + "\")");
    
    return Completes.withSuccess(Response.of(Ok, OrganizationId));
  }

  /**
   * Answer the eventual {@code Response} of defining a new Organization.
   * @return {@code Completes<Response>}
   */
  public Completes<Response> defineOrganization() {
    final String id = id();
    final String uri = "/organizations/" + id;
    
    world.defaultLogger().debug(getClass().getSimpleName() + ": startOrganization(): " + uri);
    
    return Completes.withSuccess(Response.of(Created, headers(of(Location, uri)), id));
  }

  public Completes<Response> renameOrganization(String organizationId, String name) {
    return Completes.withSuccess(Response.of(Ok, name));
  }
  
  public Completes<Response> enableOrganization(String organizationId) {
    return Completes.withSuccess(Response.of(Ok, "enabled"));
  }

  public Completes<Response> disableOrganization(String organizationId) {
    return Completes.withSuccess(Response.of(Ok, "disabled"));
  }

  /**
   * Answer the {@code Resource} of REST method handlers.
   * @return {@code Resource<?>}
   */
  public Resource<?> routes() {
    return resource("Organization Resource Fluent API",
            get("/info")
                    .handle(this::info),
            post("/organizations")
                    .handle(this::defineOrganization),
            get("/organizations/{organizationId}")
                    .param(String.class)
                    .handle(this::queryOrganization),
            patch("/organizations/{organizationId}/name")
                    .param(String.class)
                    .body(String.class)
                    .handle(this::renameOrganization),
            patch("/organizations/{organizationId}/enable")
                    .param(String.class)
                    .handle(this::enableOrganization),
            patch("/organizations/{organizationId}/disable")
                    .param(String.class)
                    .handle(this::disableOrganization));
  }

  private String id() {
    return UUID.randomUUID().toString();
  }
}
