// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Created;
import static io.vlingo.http.Response.NotFound;
import static io.vlingo.http.Response.Ok;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.actors.Address;
import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.frontservice.infra.persistence.StateStoreUserRepository;
import io.vlingo.frontservice.model.Contact;
import io.vlingo.frontservice.model.Name;
import io.vlingo.frontservice.model.Security;
import io.vlingo.frontservice.model.User;
import io.vlingo.frontservice.model.UserActor;
import io.vlingo.frontservice.model.UserRepository;
import io.vlingo.http.Response;
import io.vlingo.http.resource.ResourceHandler;

public class UserResource extends ResourceHandler {
  private final AddressFactory addressFactory;
  private final UserRepository repository;
  private final Stage stage;

  public UserResource(final World world) {
    this.addressFactory = world.addressFactory();
    this.stage = world.stageNamed("service");
    this.repository = StateStoreUserRepository.instance();
  }

  public void register(final UserData userData) {
    final Address userAddress = addressFactory.uniquePrefixedWith("u-");

    final User.State userState =
            User.from(
                    userAddress.ids(),
                    Name.from(userData.nameData.given, userData.nameData.family),
                    Contact.from(userData.contactData.emailAddress, userData.contactData.telephoneNumber),
                    Security.from(userData.publicSecurityToken));

    stage.actorFor(Definition.has(UserActor.class, Definition.parameters(userState)), User.class, userAddress);

    repository.save(userState);

    completes().with(Response.of(Created, headers(of(Location, userLocation(userState.id))), serialized(UserData.from(userState))));
  }

  public void changeContact(final String userId, final ContactData contactData) {
    stage.actorOf(addressFactory.findableBy(Integer.parseInt(userId)), User.class).after(user -> {
      if (user == null) {
        completes().with(Response.of(NotFound, userLocation(userId)));
        return;
      }

      user.withContact(new Contact(contactData.emailAddress, contactData.telephoneNumber)).after(userState -> {
        repository.save(userState);
        completes().with(Response.of(Ok, serialized(UserData.from(userState))));
      });
    });
  }

  public void changeName(final String userId, final NameData nameData) {
    stage.actorOf(addressFactory.findableBy(Integer.parseInt(userId)), User.class).after(user -> {
      if (user == null) {
        completes().with(Response.of(NotFound, userLocation(userId)));
        return;
      }
      user.withName(new Name(nameData.given, nameData.family)).after(userState -> {
        repository.save(userState);
        completes().with(Response.of(Ok, serialized(UserData.from(userState))));
      });
    });
  }

  public void queryUser(final String userId) {
    final User.State userState = repository.userOf(userId);
    if (userState.doesNotExist()) {
      completes().with(Response.of(NotFound, userLocation(userId)));
    } else {
      completes().with(Response.of(Ok, serialized(UserData.from(userState))));
    }
  }

  public void queryUsers() {
    final List<UserData> users = new ArrayList<>();
    for (final User.State userState : repository.users()) {
      users.add(UserData.from(userState));
    }
    completes().with(Response.of(Ok, serialized(users)));
  }

  private String userLocation(final String userId) {
    return "/users/" + userId;
  }
}
