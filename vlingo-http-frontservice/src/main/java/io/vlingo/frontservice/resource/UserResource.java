// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.resource;

import io.vlingo.actors.*;
import io.vlingo.common.Completes;
import io.vlingo.frontservice.data.ContactData;
import io.vlingo.frontservice.data.NameData;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.frontservice.infra.persistence.Queries;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.*;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

public class UserResource {
  private final AddressFactory addressFactory;
  private final Queries queries;
  private final Stage stage;

  public UserResource(final World world) {
    this.addressFactory = world.addressFactory();
    this.stage = world.stage();
    this.queries = QueryModelStoreProvider.instance().queries;
  }

  public Completes<Response> register(final UserData userData) {
    final Address userAddress = addressFactory.uniquePrefixedWith("u-");

    final User.UserState userState =
      User.from(
        userAddress.idString(),
        Name.from(userData.nameData.given, userData.nameData.family),
        Contact.from(userData.contactData.emailAddress, userData.contactData.telephoneNumber),
        Security.from(userData.publicSecurityToken));

    stage.actorFor(User.class, Definition.has(UserEntity.class, Definition.parameters(userState)), userAddress);

    return Completes.withSuccess(Response.of(Created, headers(of(Location, userLocation(userState.id))), serialized(UserData.from(userState))));
  }

  public Completes<Response> changeContact(final String userId, final ContactData contactData) {
    return stage.actorOf(User.class, addressFactory.from(userId))
      .andThenTo(user -> user.withContact(new Contact(contactData.emailAddress, contactData.telephoneNumber)))
      .andThenTo(userState -> Completes.withSuccess(Response.of(Ok, serialized(UserData.from(userState)))))
      .otherwise(noUser -> Response.of(NotFound, userLocation(userId)));
  }

  public Completes<Response> changeName(final String userId, final NameData nameData) {
    return stage.actorOf(User.class, addressFactory.from(userId))
      .andThenTo(user -> user.withName(new Name(nameData.given, nameData.family)))
      .andThenTo(userState -> Completes.withSuccess(Response.of(Ok, serialized(UserData.from(userState)))))
      .otherwise(noUser -> Response.of(NotFound, userLocation(userId)));
  }

  public Completes<Response> queryUser(final String userId) {
    return queries.userDataOf(userId)
      .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
      .otherwise(noData -> Response.of(NotFound, userLocation(userId)));
  }

  public Completes<Response> queryUsers() {
    return queries.usersData()
      .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))));
  }

  private String userLocation(final String userId) {
    return "/users/" + userId;
  }

  public Resource<?> routes() {
    return resource("user resource fluent api",
      post("/users")
        .body(UserData.class)
        .handle(this::register),
      patch("/users/{userId}/contact")
        .param(String.class)
        .body(ContactData.class)
        .handle(this::changeContact),
      patch("/users/{userId}/name")
        .param(String.class)
        .body(NameData.class)
        .handle(this::changeName),
      get("/users/{userId}")
        .param(String.class)
        .handle(this::queryUser),
      get("/users")
        .handle(this::queryUsers));
  }
}
