// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
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

import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.frontservice.data.ProfileData;
import io.vlingo.frontservice.infra.persistence.Queries;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.Profile;
import io.vlingo.frontservice.model.ProfileEntity;
import io.vlingo.http.Response;
import io.vlingo.http.resource.ResourceHandler;

public class ProfileResource extends ResourceHandler {
  private final AddressFactory addressFactory;
  private final Queries queries;
  private final Stage stage;

  public ProfileResource(final World world) {
    this.addressFactory = world.addressFactory();
    this.stage = world.stage();
    this.queries = QueryModelStoreProvider.instance().queries;
  }

  public void define(final String userId, final ProfileData profileData) {
    stage.actorOf(addressFactory.findableBy(Integer.parseInt(userId)), Profile.class)
      .consumeAfter(profile -> {
        queries.profileOf(userId).consumeAfter(data -> {
          completes().with(Response.of(Ok, headers(of(Location, profileLocation(userId))), serialized(data)));
        });
      })
      .otherwise(noProfile -> {
        final Profile.ProfileState profileState =
                Profile.from(
                        userId,
                        profileData.twitterAccount,
                        profileData.linkedInAccount,
                        profileData.website);

        stage().actorFor(Definition.has(ProfileEntity.class, Definition.parameters(profileState)), Profile.class);
        completes().with(Response.of(Created, serialized(ProfileData.from(profileState))));

        return noProfile;
      });
  }

  public void query(final String userId) {
    queries.profileOf(userId)
      .consumeAfter(data -> {
        completes().with(Response.of(Ok, serialized(data)));
      })
      .otherwise(noData -> {
        completes().with(Response.of(NotFound, profileLocation(userId)));
        return noData;
      });
  }

  private String profileLocation(final String userId) {
    return "/users/" + userId + "/profile";
  }
}
