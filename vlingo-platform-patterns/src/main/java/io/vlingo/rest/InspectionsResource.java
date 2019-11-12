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
public class InspectionsResource {
  private final World world;
  
  public InspectionsResource(final World world) {
    this.world = world;
  }

  /**
   * Answer the eventual {@code Response} to the GET query of a given inspection.
   * @param inspectionId the String id of the inspection to query
   * @return {@code Completes<Response>}
   */
  public Completes<Response> queryInspection(final String inspectionId) {
    world.defaultLogger().debug(getClass().getSimpleName() + ": queryInspection(\"" + inspectionId + "\")");
    
    return Completes.withSuccess(Response.of(Ok, inspectionId));
  }

  /**
   * Answer the eventual {@code Response} of starting a new inspection..
   * @return {@code Completes<Response>}
   */
  public Completes<Response> startInspection() {
    final String id = id();
    final String uri = "/inspections/" + id;
    
    world.defaultLogger().debug(getClass().getSimpleName() + ": startInspection(): " + uri);
    
    return Completes.withSuccess(Response.of(Created, headers(of(Location, uri)), id));
  }
  
  /**
   * Answer the {@code Resource} of REST method handlers.
   * @return {@code Resource<?>}
   */
  public Resource<?> routes() {
    return resource("Product resource fluent api",
            post("/inspections")
                    .handle(this::startInspection),
            get("/inspections/{inspectionId}")
                    .param(String.class)
                    .handle(this::queryInspection));
  }

  private String id() {
    return UUID.randomUUID().toString();
  }
}
