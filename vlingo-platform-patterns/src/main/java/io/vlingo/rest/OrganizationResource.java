// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.rest;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import java.util.UUID;

import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

/**
 * Example of vlingo/http Fluent API mappings.
 */
public class OrganizationResource {
  private final World world;
  
  public OrganizationResource(final World world) {
    this.world = world;
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
    final String uri = "/Organizations/" + id;
    
    world.defaultLogger().debug(getClass().getSimpleName() + ": startOrganization(): " + uri);
    
    return Completes.withSuccess(Response.of(Created, headers(of(Location, uri)), id));
  }
  
  /**
   * Answer the {@code Resource} of REST method handlers.
   * @return {@code Resource<?>}
   */
  public Resource<?> routes() {
    return resource("Organization Resource Fluent API",
            post("/Organizations")
                    .handle(this::defineOrganization),
            get("/Organizations/{OrganizationId}")
                    .param(String.class)
                    .handle(this::queryOrganization));
  }

  private String id() {
    return UUID.randomUUID().toString();
  }
}
