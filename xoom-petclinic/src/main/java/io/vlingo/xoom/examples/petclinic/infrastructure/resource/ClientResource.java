package io.vlingo.xoom.examples.petclinic.infrastructure.resource;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

import io.vlingo.xoom.examples.petclinic.model.ContactInformation;
import io.vlingo.xoom.examples.petclinic.model.client.ClientEntity;
import io.vlingo.xoom.examples.petclinic.model.client.Client;
import io.vlingo.xoom.examples.petclinic.infrastructure.ClientData;
import io.vlingo.xoom.examples.petclinic.model.Fullname;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.ClientQueries;

import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.common.Completes;
import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;

/**
 * See <a href="https://docs.vlingo.io/xoom-turbo/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class ClientResource extends DynamicResourceHandler {
  private final ClientQueries $queries;

  public ClientResource(final Stage stage, final ClientQueries clientQueries) {
      super(stage);
      this.$queries = clientQueries;
  }

  public Completes<Response> changeName(final String id, final ClientData data) {
    final Fullname name = Fullname.of(data.name.first, data.name.last);
    return resolve(id)
            .andThenTo(client -> client.changeName(name))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(ClientData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> changeContactInformation(final String id, final ClientData data) {
    final ContactInformation contact = ContactInformation.of(data.contact.postalAddress, data.contact.telephone);
    return resolve(id)
            .andThenTo(client -> client.changeContactInformation(contact))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(ClientData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> register(final ClientData data) {
    final Fullname name = Fullname.of(data.name.first, data.name.last);
    final ContactInformation contact = ContactInformation.of(data.contact.postalAddress, data.contact.telephone);
    return Client.register(stage(), name, contact)
      .andThenTo(state -> Completes.withSuccess(Response.of(Created, headers(of(Location, location(state.id))), serialized(ClientData.from(state))))
      .otherwise(arg -> Response.of(NotFound))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }

  public Completes<Response> clients() {
    return $queries.clients()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> client(String id) {
    return $queries.clientOf(id)
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  @Override
  public Resource<?> routes() {
     return resource("ClientResource",
        io.vlingo.xoom.http.resource.ResourceBuilder.patch("/clients/{id}/name")
            .param(String.class)
            .body(ClientData.class)
            .handle(this::changeName),
        io.vlingo.xoom.http.resource.ResourceBuilder.patch("/clients/{id}/contact")
            .param(String.class)
            .body(ClientData.class)
            .handle(this::changeContactInformation),
        io.vlingo.xoom.http.resource.ResourceBuilder.post("/clients")
            .body(ClientData.class)
            .handle(this::register),
        io.vlingo.xoom.http.resource.ResourceBuilder.get("/clients")
            .handle(this::clients),
        io.vlingo.xoom.http.resource.ResourceBuilder.get("/clients/{id}")
            .param(String.class)
            .handle(this::client)
     );
  }

  private String location(final String id) {
    return "/clients/" + id;
  }

  private Completes<Client> resolve(final String id) {
    return stage().actorOf(Client.class, stage().addressFactory().from(id), Definition.has(ClientEntity.class, Definition.parameters(id)));
  }

}
