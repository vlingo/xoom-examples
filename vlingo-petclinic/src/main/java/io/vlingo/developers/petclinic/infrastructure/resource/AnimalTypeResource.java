package io.vlingo.developers.petclinic.infrastructure.resource;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.DynamicResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeEntity;
import io.vlingo.developers.petclinic.model.animaltype.AnimalType;
import io.vlingo.developers.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.developers.petclinic.infrastructure.persistence.AnimalTypeQueries;
import io.vlingo.developers.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;

import io.vlingo.http.Response;
import io.vlingo.common.Completes;
import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class AnimalTypeResource extends DynamicResourceHandler {
  private final AnimalTypeQueries $queries;

  public AnimalTypeResource(final Stage stage, final AnimalTypeQueries animalTypeQueries) {
      super(stage);
      this.$queries = animalTypeQueries;
  }

  public Completes<Response> rename(final String id, final AnimalTypeData data) {
    return resolve(id)
            .andThenTo(animalType -> animalType.rename(data.name))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(AnimalTypeData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> offerTreatmentFor(final AnimalTypeData data) {
    return AnimalType.offerTreatmentFor(stage(), data.name)
      .andThenTo(state -> Completes.withSuccess(Response.of(Created, headers(of(Location, location(state.id))), serialized(AnimalTypeData.from(state))))
      .otherwise(arg -> Response.of(NotFound))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }

  public Completes<Response> animalTypes() {
    return $queries.animalTypes()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> animalType(String id) {
    return $queries.animalTypeOf(id)
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  @Override
  public Resource<?> routes() {
     return resource("AnimalTypeResource",
        io.vlingo.http.resource.ResourceBuilder.put("/animalTypes/{id}")
            .param(String.class)
            .body(AnimalTypeData.class)
            .handle(this::rename),
        io.vlingo.http.resource.ResourceBuilder.post("/animalTypes")
            .body(AnimalTypeData.class)
            .handle(this::offerTreatmentFor),
        io.vlingo.http.resource.ResourceBuilder.get("/animalTypes")
            .handle(this::animalTypes),
        io.vlingo.http.resource.ResourceBuilder.get("/animalTypes/{id}")
            .param(String.class)
            .handle(this::animalType)
     );
  }

  private String location(final String id) {
    return "/animalTypes/" + id;
  }

  private Completes<AnimalType> resolve(final String id) {
    return stage().actorOf(AnimalType.class, stage().addressFactory().from(id), Definition.has(AnimalTypeEntity.class, Definition.parameters(id)));
  }

}
