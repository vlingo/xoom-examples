package io.vlingo.developers.petclinic.infrastructure.resource;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.DynamicResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import io.vlingo.developers.petclinic.infrastructure.VeterinarianData;
import io.vlingo.developers.petclinic.model.ContactInformation;
import io.vlingo.developers.petclinic.model.client.Specialty;
import io.vlingo.developers.petclinic.infrastructure.persistence.VeterinarianQueries;
import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianEntity;
import io.vlingo.developers.petclinic.model.veterinarian.Veterinarian;
import io.vlingo.developers.petclinic.model.Fullname;
import io.vlingo.developers.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;

import io.vlingo.http.Response;
import io.vlingo.common.Completes;
import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class VeterinarianResource extends DynamicResourceHandler {
  private final VeterinarianQueries $queries;

  public VeterinarianResource(final Stage stage) {
      super(stage);
      this.$queries = QueryModelStateStoreProvider.instance().veterinarianQueries;
  }

  public Completes<Response> changeName(final String id, final VeterinarianData data) {
    final Fullname name = Fullname.of(data.name.first, data.name.last);
    return resolve(id)
            .andThenTo(veterinarian -> veterinarian.changeName(name))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(VeterinarianData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> changeContactInformation(final String id, final VeterinarianData data) {
    final ContactInformation contact = ContactInformation.of(data.contact.postalAddress, data.contact.telephone);
    return resolve(id)
            .andThenTo(veterinarian -> veterinarian.changeContactInformation(contact))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(VeterinarianData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> specializesIn(final String id, final VeterinarianData data) {
    final Specialty specialty = Specialty.of(data.specialty.specialtyTypeId);
    return resolve(id)
            .andThenTo(veterinarian -> veterinarian.specializesIn(specialty))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(VeterinarianData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> register(final VeterinarianData data) {
    final Fullname name = Fullname.of(data.name.first, data.name.last);
    final ContactInformation contact = ContactInformation.of(data.contact.postalAddress, data.contact.telephone);
    final Specialty specialty = Specialty.of(data.specialty.specialtyTypeId);
    return Veterinarian.register(stage(), name, contact, specialty)
      .andThenTo(state -> Completes.withSuccess(Response.of(Created, headers(of(Location, location(state.id))), serialized(VeterinarianData.from(state))))
      .otherwise(arg -> Response.of(NotFound, location()))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }

  public Completes<Response> veterinarians() {
    return $queries.veterinarians()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> veterinarian(String id) {
    return $queries.veterinarianOf(id)
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  @Override
  public Resource<?> routes() {
     return resource("VeterinarianResource",
        io.vlingo.http.resource.ResourceBuilder.patch("/veterinarians/{id}/name")
            .param(String.class)
            .body(VeterinarianData.class)
            .handle(this::changeName),
        io.vlingo.http.resource.ResourceBuilder.patch("/veterinarians/{id}/contact")
            .param(String.class)
            .body(VeterinarianData.class)
            .handle(this::changeContactInformation),
        io.vlingo.http.resource.ResourceBuilder.patch("/veterinarians/{id}/specialty")
            .param(String.class)
            .body(VeterinarianData.class)
            .handle(this::specializesIn),
        io.vlingo.http.resource.ResourceBuilder.post("/veterinarians")
            .body(VeterinarianData.class)
            .handle(this::register),
        io.vlingo.http.resource.ResourceBuilder.get("/veterinarians")
            .handle(this::veterinarians),
        io.vlingo.http.resource.ResourceBuilder.get("/veterinarians/{id}")
            .param(String.class)
            .handle(this::veterinarian)
     );
  }

  private String location() {
    return location("");
  }

  private String location(final String id) {
    return "/veterinarians/" + id;
  }

  private Completes<Veterinarian> resolve(final String id) {
    return stage().actorOf(Veterinarian.class, stage().addressFactory().from(id), Definition.has(VeterinarianEntity.class, Definition.parameters(id)));
  }

}
