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
import java.util.List;

import io.vlingo.actors.Actor;
import io.vlingo.actors.AddressFactory;
import io.vlingo.frontservice.model.User;
import io.vlingo.http.Request;
import io.vlingo.http.RequestHeader;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Client;
import io.vlingo.http.resource.Client.Configuration;
import io.vlingo.http.resource.ResponseConsumer;
import io.vlingo.http.resource.sse.MessageEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.Projection;
import io.vlingo.lattice.model.projection.ProjectionControl;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;

public class PrivateTokenSynchronizerActor extends Actor implements Projection {
  private static final int Identities = 0;
  private static final int Token = 1;
  private static final int UserId = 0;
  private static final int ProjectionId = 1;

  private static final String DataAttributesSeparator = "\n";
  private static final String IdentitiesSeparator = ":";

  private final AddressFactory addressFactory;
  private Client client;
  private ProjectionControl control;

  public PrivateTokenSynchronizerActor() {
    this.addressFactory = stage().world().addressFactory();
    this.client = client();
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    this.control = control;
    final User.UserState state = projectable.object();
    final String correlationId = state.id + IdentitiesSeparator + projectable.projectionId();

    if (client == null && client() == null) {
      logger().log("PrivateTokenSynchronizerActor: Currently no connection to backservice.");
      return;
    }

    logger().log("REQUESTING TOKEN: " + correlationId);

    client.requestWith(
            Request
              .has(GET)
              .and(URI.create("/tokens/" + state.security.publicToken))
              .and(host("localhost"))
              .and(RequestHeader.of(RequestHeader.XCorrelationID, correlationId)))
          .consumeAfter(response -> {
             switch (response.status) {
             case Ok:
               logger().log("RESPONDED FOR TOKEN: " + correlationId);
               break;
             default:
               logger().log("Failed token request for user: " + state.id + " because: " + response.status);
               break;
             }
           });
  }

  private Client client() {
    try {
      client = Client.using(Configuration.defaultedKeepAliveExceptFor(
              stage(),
              Address.from(Host.of(System.getProperty("BACKSERVICE_HOST", "localhost")), 8082, AddressType.NONE),
              new ResponseConsumer() {
                @Override
                public void consume(final Response response) {
                  logger().log("Unknown response received: " + response.status);
                }
              }));

      subscribeToEvents();

      return client;

    } catch (Exception e) {
      logger().log("The client could not be created.", e);
      return client;
    }
  }

  private void subscribeToEvents() {
    client.requestWith(
            Request
              .has(GET)
              .and(URI.create("/vaultstreams/tokens"))
              .and(host("localhost"))
              .and(RequestHeader.accept("text/event-stream"))
              .and(RequestHeader.correlationId(getClass().getSimpleName() + "-tokens")))
            .consumeAfter(response -> {
              switch (response.status) {
              case Ok: {
                final List<MessageEvent> events = MessageEvent.from(response);
                for (MessageEvent event : events) {
                  logger().log("EVENT: " + event);
                  final String[] attributes = event.data.split(DataAttributesSeparator);
                  final String identities[] = attributes[Identities].split(IdentitiesSeparator);
                  final io.vlingo.actors.Address address = addressFactory.from(identities[UserId]);
                  stage().actorOf(address, User.class)
                    .consumeAfter(user -> {
                      user.attachPrivateToken(attributes[Token]);
                      control.confirmProjected(identities[ProjectionId]);
                      logger().log("USER TOKEN SYNCHRONIZED: " + identities[UserId] + " WITH: " + attributes[Token]);
                    });
                }
               break;
              }
              default:
                logger().log("Unexpected: " + response.status);
                break;
              }
            })
            .repeat();
  }
}
