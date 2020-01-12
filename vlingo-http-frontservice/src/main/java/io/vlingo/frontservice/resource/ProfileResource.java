// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.put;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.infra.persistence.Queries;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.Profile;
import io.vlingo.frontservice.model.ProfileEntity;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

public class ProfileResource {
  private final AddressFactory addressFactory;
  private final Queries queries;
  private final Stage stage;

  public ProfileResource(final World world) {
    this.addressFactory = world.addressFactory();
    this.stage = world.stage();
    this.queries = QueryModelStoreProvider.instance().queries;
  }

  public Completes<Response> define(final String userId, final ProfileData profileData) {
    return stage.actorOf(Profile.class, addressFactory.findableBy(Integer.parseInt(userId)))
      .andThenTo(profile -> queries.profileOf(userId))
      .andThenTo(data -> Completes.withSuccess(Response.of(Ok, headers(of(Location, profileLocation(userId))), serialized(data))))
      .otherwise(noProfile -> {
        final Profile.ProfileState profileState =
          Profile.from(
            userId,
            profileData.twitterAccount,
            profileData.linkedInAccount,
            profileData.website);
        stage.actorFor(Profile.class, Definition.has(ProfileEntity.class, Definition.parameters(profileState)));
        return Response.of(Created, serialized(ProfileData.from(profileState)));
      });
  }

  public Completes<Response> query(final String userId) {
    return queries.profileOf(userId)
      .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
      .otherwise(noData -> Response.of(NotFound, profileLocation(userId)));
  }

  private String profileLocation(final String userId) {
    return "/users/" + userId + "/profile";
  }

  public Resource<?> routes() {
    return resource("profile resource fluent api",
      put("/users/{userId}/profile")
        .param(String.class)
        .body(ProfileData.class)
        .handle(this::define),
      get("/users/{userId}/profile")
        .param(String.class)
        .handle(this::query));
  }
}
