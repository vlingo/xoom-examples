// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import static io.vlingo.http.Method.GET;
import static io.vlingo.http.RequestHeader.host;
import static io.vlingo.http.Response.Status.RequestTimeout;

import java.net.URI;

import io.vlingo.actors.Completes;
import io.vlingo.actors.CompletesEventually;
import io.vlingo.http.Request;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Client;
import io.vlingo.http.resource.Client.Configuration;
import io.vlingo.http.resource.ResponseConsumer;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;

public class UserEntity extends StatefulEntity<User.State,String> implements User, PrivateTokenConsumer {
  private User.State state;
  private int stateVersion;

  public UserEntity(final User.State state) {
    this.state = state;

    retrievePrivateToken();
  }

  @Override
  public void start() {
    if (state.isIdentifiedOnly()) {
      restore((state, version) -> state(state, version));
    } else {
      preserve(state, "User:new");
    }
  }


  //=====================================
  // User
  //=====================================

  public Completes<User.State> withContact(final Contact contact) {
    final CompletesEventually completes = completesEventually();
    final User.State transitioned = state.withContact(contact);
    preserve(transitioned, "User:contact", (state, version) -> {
      state(state, version);
      completes.with(state);
    });
    return completes(); // unanswered until preserved
  }

  public Completes<User.State> withName(final Name name) {
    final CompletesEventually completes = completesEventually();
    final User.State transitioned = state.withName(name);
    preserve(transitioned, "User:name", (state, version) -> {
      state(state, version);
      completes.with(state);
    });
    return completes(); // unanswered until preserved
  }


  //=====================================
  // PrivateTokenConsumer
  //=====================================

  @Override
  public void consumePrivateToken(final String privateToken) {
    final User.State transitioned = state.withSecurity(state.security.withPrivateToken(privateToken));
    preserve(transitioned);
  }


  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  public String id() {
    return state.id;
  }

  @Override
  public void state(final State state, final int stateVersion) {
    this.state = state;
    this.stateVersion = stateVersion;
  }

  @Override
  public Class<?> stateType() {
    return User.State.class;
  }

  @Override
  public int stateVersion() {
    return stateVersion;
  }

  @Override
  public int typeVersion() {
    return 1;
  }


  //=====================================
  // internal interface
  //=====================================

  private void retrievePrivateToken() {
    // NOTE: This is a temporary and not-recommended approach

    client().requestWith(
            Request
              .has(GET)
              .and(URI.create("/tokens/" + state.security.publicToken))
              .and(host("localhost")))
          .after(response -> {
                   switch (response.status) {
                   case Ok:
                     logger().log("Private token received: " + response.entity.content);
                     selfAs(PrivateTokenConsumer.class).consumePrivateToken(response.entity.content);
                     break;
                   case RequestTimeout:
                     logger().log("Timeout; private token not received.");
                     break;
                   default:
                     logger().log("Unexpected: " + response.status);
                     break;
                   }
                 },
                 5000,
                 Response.of(RequestTimeout));
  }

  private Client client() {
    try {
      return Client.using(Configuration.defaultedExceptFor(
              stage(),
              Address.from(Host.of("localhost"), 8082, AddressType.NONE),
              new ResponseConsumer() {
                @Override
                public void consume(final Response response) {
                  logger().log("Unknown response received: " + response.status);
                }
              }));
    } catch (Exception e) {
      final String message = "The client could not be created.";
      logger().log(message, e);
      throw new IllegalStateException(message, e);
    }
  }
}
