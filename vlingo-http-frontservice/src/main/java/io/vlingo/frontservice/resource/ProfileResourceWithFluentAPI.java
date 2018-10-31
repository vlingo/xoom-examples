// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.resource;

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

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

public class ProfileResourceWithFluentAPI {
  private final AddressFactory addressFactory;
  private final Queries queries;
  private final Stage stage;

  public ProfileResourceWithFluentAPI(final World world) {
    this.addressFactory = world.addressFactory();
    this.stage = world.stage();
    this.queries = QueryModelStoreProvider.instance().queries;
  }

  public Completes<Response> define(final String userId, final ProfileData profileData) {
    return stage.actorOf(addressFactory.findableBy(Integer.parseInt(userId)), Profile.class)
      .andThenInto(profile -> queries.profileOf(userId))
      .andThenInto(data -> Completes.withSuccess(Response.of(Ok, headers(of(Location, profileLocation(userId))), serialized(data))))
      .otherwise(noProfile -> {
        final Profile.ProfileState profileState =
          Profile.from(
            userId,
            profileData.twitterAccount,
            profileData.linkedInAccount,
            profileData.website);
        stage.actorFor(Definition.has(ProfileEntity.class, Definition.parameters(profileState)), Profile.class);
        return Response.of(Created, serialized(ProfileData.from(profileState)));
      });
  }

  public Completes<Response> query(final String userId) {
    return queries.profileOf(userId)
      .andThenInto(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
      .otherwise(noData -> Response.of(NotFound, profileLocation(userId)));
  }

  private String profileLocation(final String userId) {
    return "/users/" + userId + "/profile";
  }

  public Resource routes() {
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
