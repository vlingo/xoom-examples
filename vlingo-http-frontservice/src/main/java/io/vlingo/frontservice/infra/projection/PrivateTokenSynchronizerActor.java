// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import static io.vlingo.http.Method.GET;
import static io.vlingo.http.RequestHeader.host;

import java.net.URI;

import io.vlingo.actors.Actor;
import io.vlingo.actors.AddressFactory;
import io.vlingo.frontservice.model.User;
import io.vlingo.http.Request;
import io.vlingo.http.RequestHeader;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Client;
import io.vlingo.http.resource.Client.Configuration;
import io.vlingo.http.resource.ResponseConsumer;
import io.vlingo.symbio.projection.Projectable;
import io.vlingo.symbio.projection.Projection;
import io.vlingo.symbio.projection.ProjectionControl;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;

public class PrivateTokenSynchronizerActor extends Actor implements Projection {
  private final AddressFactory addressFactory;
  private final Client client;

  public PrivateTokenSynchronizerActor() {
    this.addressFactory = stage().world().addressFactory();
    this.client = client();
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    final User.State state = projectable.object();
    final Client requestingClient = client == null ? client() : client;

    if (requestingClient == null) {
      logger().log("PrivateTokenSynchronizerActor: Currently no connection to backservice.");
      return;
    }

    requestingClient.requestWith(
            Request
              .has(GET)
              .and(URI.create("/tokens/" + state.security.publicToken))
              .and(host("localhost"))
              .and(RequestHeader.of(RequestHeader.XCorrelationID, state.id)))
          .after(response -> {
             switch (response.status) {
             case Ok: {
               final String userId = state.id;
               final io.vlingo.actors.Address address = addressFactory.findableBy(Integer.parseInt(userId));
               stage().actorOf(address, User.class).after(user -> {
                 if (user != null) {
                   user.attachPrivateToken(response.entity.content);
                   control.confirmProjected(projectable.projectionId());
                 }
               });
               break;
             }
             default:
               logger().log("Unexpected: " + response.status);
               break;
             }
           });
  }

  private Client client() {
    try {
      return Client.using(Configuration.defaultedExceptFor(
              stage(),
              Address.from(Host.of(System.getProperty("BACKSERVICE_HOST", "localhost")), 8082, AddressType.NONE),
              new ResponseConsumer() {
                @Override
                public void consume(final Response response) {
                  logger().log("Unknown response received: " + response.status);
                }
              }));
    } catch (Exception e) {
      logger().log("The client could not be created.", e);
      return null;
    }
  }
}
