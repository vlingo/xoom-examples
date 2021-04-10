// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.resource;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.Created;
import static io.vlingo.xoom.http.Response.Status.NotFound;
import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.ResponseHeader.headers;
import static io.vlingo.xoom.http.ResponseHeader.of;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;
import static io.vlingo.xoom.http.resource.ResourceBuilder.patch;
import static io.vlingo.xoom.http.resource.ResourceBuilder.post;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.AddressFactory;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.frontservice.data.ContactData;
import io.vlingo.xoom.examples.frontservice.data.NameData;
import io.vlingo.xoom.examples.frontservice.data.UserData;
import io.vlingo.xoom.examples.frontservice.infra.persistence.Queries;
import io.vlingo.xoom.examples.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.xoom.examples.frontservice.model.Contact;
import io.vlingo.xoom.examples.frontservice.model.Name;
import io.vlingo.xoom.examples.frontservice.model.Security;
import io.vlingo.xoom.examples.frontservice.model.User;
import io.vlingo.xoom.examples.frontservice.model.UserEntity;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.Resource;

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
      .otherwise(noUser -> Response.of(NotFound));
  }

  public Completes<Response> changeName(final String userId, final NameData nameData) {
    return stage.actorOf(User.class, addressFactory.from(userId))
      .andThenTo(user -> user.withName(new Name(nameData.given, nameData.family)))
      .andThenTo(userState -> Completes.withSuccess(Response.of(Ok, serialized(UserData.from(userState)))))
      .otherwise(noUser -> Response.of(NotFound));
  }

  public Completes<Response> queryUser(final String userId) {
    return queries.userDataOf(userId)
      .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
      .otherwise(noData -> Response.of(NotFound));
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
