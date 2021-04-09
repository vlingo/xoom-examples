// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
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
  private static final String BACKSERVICE_HOST = System.getenv("BACKSERVICE_HOST")!=null?System.getenv("BACKSERVICE_HOST"):"localhost";

  private final AddressFactory addressFactory;
  private Client client;
  private ProjectionControl control;

  public PrivateTokenSynchronizerActor() {
    this.addressFactory = stage().world().addressFactory();
    this.client = client();
    subscribeToEvents(this.client);
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    this.control = control;
    final User.UserState state = projectable.object();
    final String correlationId = state.id + IdentitiesSeparator + projectable.projectionId();

    if (client == null && client() == null) {
      logger().warn("PrivateTokenSynchronizerActor: Currently no connection to backservice.");
      return;
    }

    logger().debug("REQUESTING TOKEN: " + correlationId);

    final Client tokenRequestClient = client();
    
    tokenRequestClient.requestWith(
            Request
              .has(GET)
              .and(URI.create("/tokens/" + state.security.publicToken))
              .and(host(BACKSERVICE_HOST))
              .and(RequestHeader.of(RequestHeader.XCorrelationID, correlationId)))
          .andThenConsume(response -> {
        	 tokenRequestClient.close();
             switch (response.status) {
             case Ok:
               logger().debug("STARTING PROCESSING FOR TOKEN: " + correlationId);
               break;
             default:
               logger().error("Failed token request for user: " + state.id + " because: " + response.status);
               break;
             }
           });
  }

  private Client client() {
    try {
      final Client client = Client.using(Configuration.defaultedKeepAliveExceptFor(
              stage(),
              Address.from(Host.of(BACKSERVICE_HOST), 8082, AddressType.NONE),
              new ResponseConsumer() {
                @Override
                public void consume(final Response response) {
                  logger().error("Unknown response received: " + response.status);
                }
              }));

      return client;

    } catch (Exception e) {
      logger().error("The client could not be created.", e);
      return client;
    }
  }

  private void subscribeToEvents(final Client client) {
    client.requestWith(
            Request
              .has(GET)
              .and(URI.create("/vaultstreams/tokens"))
              .and(host(BACKSERVICE_HOST))
              .and(RequestHeader.accept("text/event-stream"))
              .and(RequestHeader.correlationId(getClass().getSimpleName() + "-tokens")))
            .andThenConsume(response -> {
              switch (response.status) {
              case Ok:
                attachPrivateTokenFrom(response);
               break;
              default:
                logger().error("Unexpected: " + response.status);
                break;
              }
            })
            .repeat();
  }

  private void attachPrivateTokenFrom(final Response response) {
    final List<MessageEvent> events = MessageEvent.from(response);
    for (final MessageEvent event : events) {
      logger().debug("EVENT: " + event);
      final SecurityTokenInfo eventData = securityTokenInfoFrom(event);
      stage().actorOf(User.class, eventData.address)
        .andThenConsume(user -> {
          user.attachPrivateToken(eventData.securityToken);
          control.confirmProjected(eventData.projectionId);
          logger().debug("USER TOKEN SYNCHRONIZED: " + eventData.userId + " WITH: " + eventData.securityToken);
        });
    }
  }

  private SecurityTokenInfo securityTokenInfoFrom(final MessageEvent event) {
    final String[] attributes = event.data.split(DataAttributesSeparator);
    final String[] identities = attributes[Identities].split(IdentitiesSeparator);
    final io.vlingo.actors.Address address = addressFactory.from(identities[UserId]);
    return new SecurityTokenInfo(address, identities[UserId], identities[ProjectionId], attributes[Token]);
  }

  private class SecurityTokenInfo {
    private final io.vlingo.actors.Address address;
    private final String projectionId;
    private final String securityToken;
    private final String userId;

    private SecurityTokenInfo(io.vlingo.actors.Address address, final String userId, final String projectionId, final String securityToken) {
      this.address = address;
      this.userId = userId;
      this.projectionId = projectionId;
      this.securityToken = securityToken;
    }
  }
}
