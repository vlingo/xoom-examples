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

import io.vlingo.actors.Address;
import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.frontservice.data.ContactData;
import io.vlingo.frontservice.data.NameData;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.frontservice.infra.persistence.Queries;
import io.vlingo.frontservice.infra.persistence.QueryModelStoreProvider;
import io.vlingo.frontservice.model.Contact;
import io.vlingo.frontservice.model.Name;
import io.vlingo.frontservice.model.Security;
import io.vlingo.frontservice.model.User;
import io.vlingo.frontservice.model.UserEntity;
import io.vlingo.http.Response;
import io.vlingo.http.resource.ResourceHandler;

public class UserResource extends ResourceHandler {
  private final AddressFactory addressFactory;
  private final Queries queries;
  private final Stage stage;

  public UserResource(final World world) {
    this.addressFactory = world.addressFactory();
    this.stage = world.stage();
    this.queries = QueryModelStoreProvider.instance().queries;
  }

  public void register(final UserData userData) {
    final Address userAddress = addressFactory.uniquePrefixedWith("u-");

    final User.UserState userState =
            User.from(
                    userAddress.idString(),
                    Name.from(userData.nameData.given, userData.nameData.family),
                    Contact.from(userData.contactData.emailAddress, userData.contactData.telephoneNumber),
                    Security.from(userData.publicSecurityToken));

    stage.actorFor(Definition.has(UserEntity.class, Definition.parameters(userState)), User.class, userAddress);

    completes().with(Response.of(Created, headers(of(Location, userLocation(userState.id))), serialized(UserData.from(userState))));
  }

  public void changeContact(final String userId, final ContactData contactData) {
    stage.actorOf(addressFactory.findableBy(addressFactory.from(userId)), User.class)
      .consumeAfter(user -> {
        user.withContact(new Contact(contactData.emailAddress, contactData.telephoneNumber))
          .consumeAfter(userState -> {
            completes().with(Response.of(Ok, serialized(UserData.from(userState))));
          });
      })
      .otherwise(noUser -> {
        completes().with(Response.of(NotFound, userLocation(userId)));
        return noUser;
        });
  }

  public void changeName(final String userId, final NameData nameData) {
    stage.actorOf(addressFactory.findableBy(addressFactory.from(userId)), User.class)
      .consumeAfter(user -> {
        user.withName(new Name(nameData.given, nameData.family))
          .consumeAfter(userState -> {
            completes().with(Response.of(Ok, serialized(UserData.from(userState))));
          });
        })
      .otherwise(noUser -> {
        completes().with(Response.of(NotFound, userLocation(userId)));
        return noUser;
        });
  }

  public void queryUser(final String userId) {
    queries.userDataOf(userId)
      .consumeAfter(data -> {
        completes().with(Response.of(Ok, serialized(data)));
      })
    .otherwise(noData -> {
      completes().with(Response.of(NotFound, userLocation(userId)));
      return noData;
      });
  }

  public void queryUsers() {
    queries.usersData()
      .consumeAfter(data -> {
        completes().with(Response.of(Ok, serialized(data)));
      });
  }

  private String userLocation(final String userId) {
    return "/users/" + userId;
  }
}
