// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

import static io.vlingo.http.Method.GET;
import static io.vlingo.http.RequestHeader.host;
import static io.vlingo.http.Response.Ok;
import static io.vlingo.http.Response.RequestTimeout;

import java.net.URI;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Completes;
import io.vlingo.http.Request;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Client;
import io.vlingo.http.resource.Client.Configuration;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;
import io.vlingo.http.resource.ResponseConsumer;

public class UserActor extends Actor implements User, PrivateTokenConsumer {
  private User.State state;

  public UserActor(final User.State state) {
    this.state = state;

    retrievePrivateToken();
  }

  public Completes<User.State> withContact(final Contact contact) {
    state = state.withContact(contact);
    return completes().with(state);
  }

  public Completes<User.State> withName(final Name name) {
    state = state.withName(name);
    return completes().with(state);
  }

  @Override
  public void consumePrivateToken(final String privateToken) {
    state = state.withSecurity(state.security.withPrivateToken(privateToken));
  }

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
                     System.out.println("Private token received: " + response.entity.content);
                     selfAs(PrivateTokenConsumer.class).consumePrivateToken(response.entity.content);
                     break;
                   case RequestTimeout:
                     System.out.println("Timeout; private token not received.");
                     break;
                   default:
                     System.out.println("Unexpected: " + response.status);
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
                  System.out.println("Unknown response received: " + response.status);
                }
              }));
    } catch (Exception e) {
      final String message = "The client could not be created.";
      logger().log(message, e);
      throw new IllegalStateException(message, e);
    }
  }
}
