package io.vlingo.developers.petclinic.infrastructure.resource;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.DynamicResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import io.vlingo.developers.petclinic.model.client.Kind;
import io.vlingo.developers.petclinic.model.pet.Pet;
import io.vlingo.developers.petclinic.infrastructure.PetData;
import io.vlingo.developers.petclinic.model.client.Name;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetQueries;
import io.vlingo.developers.petclinic.model.client.Owner;
import io.vlingo.developers.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.developers.petclinic.model.pet.PetEntity;

import io.vlingo.http.Response;
import io.vlingo.common.Completes;
import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class PetResource extends DynamicResourceHandler {
  private final PetQueries $queries;

  public PetResource(final Stage stage) {
      super(stage);
      this.$queries = QueryModelStateStoreProvider.instance().petQueries;
  }

  public Completes<Response> changeName(final String id, final PetData data) {
    final Name name = Name.of(data.name.value);
    return resolve(id)
            .andThenTo(pet -> pet.changeName(name))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(PetData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> recordBirth(final String id, final PetData data) {
    return resolve(id)
            .andThenTo(pet -> pet.recordBirth(data.birth))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(PetData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> recordDeath(final String id, final PetData data) {
    return resolve(id)
            .andThenTo(pet -> pet.recordDeath(data.death))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(PetData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> correctKind(final String id, final PetData data) {
    final Kind kind = Kind.of(data.kind.animalTypeId);
    return resolve(id)
            .andThenTo(pet -> pet.correctKind(kind))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(PetData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> changeOwner(final String id, final PetData data) {
    final Owner owner = Owner.of(data.owner.clientId);
    return resolve(id)
            .andThenTo(pet -> pet.changeOwner(owner))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(PetData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> register(final PetData data) {
    final Name name = Name.of(data.name.value);
    final Kind kind = Kind.of(data.kind.animalTypeId);
    final Owner owner = Owner.of(data.owner.clientId);
    return Pet.register(stage(), name, data.birth, kind, owner)
      .andThenTo(state -> Completes.withSuccess(Response.of(Created, headers(of(Location, location(state.id))), serialized(PetData.from(state))))
      .otherwise(arg -> Response.of(NotFound, location()))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }

  public Completes<Response> pets() {
    return $queries.pets()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> pet(String id) {
    return $queries.petOf(id)
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> petsForOwner(String ownerId) {
    return $queries.petsForOwner(ownerId)
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  @Override
  public Resource<?> routes() {
     return resource("PetResource",
        io.vlingo.http.resource.ResourceBuilder.patch("/pets/{id}/name")
            .param(String.class)
            .body(PetData.class)
            .handle(this::changeName),
        io.vlingo.http.resource.ResourceBuilder.patch("/pets/{id}/birth")
            .param(String.class)
            .body(PetData.class)
            .handle(this::recordBirth),
        io.vlingo.http.resource.ResourceBuilder.patch("/pets/{id}/death")
            .param(String.class)
            .body(PetData.class)
            .handle(this::recordDeath),
        io.vlingo.http.resource.ResourceBuilder.patch("/pets/{id}/kind")
            .param(String.class)
            .body(PetData.class)
            .handle(this::correctKind),
        io.vlingo.http.resource.ResourceBuilder.patch("/pets/{id}/owner")
            .param(String.class)
            .body(PetData.class)
            .handle(this::changeOwner),
        io.vlingo.http.resource.ResourceBuilder.post("/pets")
            .body(PetData.class)
            .handle(this::register),
        io.vlingo.http.resource.ResourceBuilder.get("/pets")
            .handle(this::pets),
        io.vlingo.http.resource.ResourceBuilder.get("/pets/{id}")
            .param(String.class)
            .handle(this::pet),
        io.vlingo.http.resource.ResourceBuilder.get("/pets/owners/{id}")
            .param(String.class)
            .handle(this::petsForOwner)
     );
  }

  private String location() {
    return location("");
  }

  private String location(final String id) {
    return "/pets" + id;
  }

  private Completes<Pet> resolve(final String id) {
    return stage().actorOf(Pet.class, stage().addressFactory().from(id), Definition.has(PetEntity.class, Definition.parameters(id)));
  }

}
