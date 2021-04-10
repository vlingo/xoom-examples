package io.vlingo.xoom.examples.petclinic.infrastructure.resource;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.SpecialtyTypeQueries;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyType;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeEntity;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

/**
 * See <a href="https://docs.vlingo.io/xoom-turbo/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class SpecialtyTypeResource extends DynamicResourceHandler {
  private final SpecialtyTypeQueries $queries;

  public SpecialtyTypeResource(final Stage stage, final SpecialtyTypeQueries specialtyTypeQueries) {
      super(stage);
      this.$queries = specialtyTypeQueries;
  }

  public Completes<Response> rename(final String id, final SpecialtyTypeData data) {
    return resolve(id)
            .andThenTo(specialtyType -> specialtyType.rename(data.name))
            .andThenTo(state -> Completes.withSuccess(Response.of(Ok, serialized(SpecialtyTypeData.from(state)))))
            .otherwise(noGreeting -> Response.of(NotFound, location(id)))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> offer(final SpecialtyTypeData data) {
    return SpecialtyType.offer(stage(), data.name)
      .andThenTo(state -> Completes.withSuccess(Response.of(Created, headers(of(Location, location(state.id))), serialized(SpecialtyTypeData.from(state))))
      .otherwise(arg -> Response.of(NotFound))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }

  public Completes<Response> specialtyTypes() {
    return $queries.specialtyTypes()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> specialtyType(String id) {
    return $queries.specialtyTypeOf(id)
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  @Override
  public Resource<?> routes() {
     return resource("SpecialtyTypeResource",
        io.vlingo.xoom.http.resource.ResourceBuilder.post("/specialties/{id}")
            .param(String.class)
            .body(SpecialtyTypeData.class)
            .handle(this::rename),
        io.vlingo.xoom.http.resource.ResourceBuilder.post("/specialties")
            .body(SpecialtyTypeData.class)
            .handle(this::offer),
        io.vlingo.xoom.http.resource.ResourceBuilder.get("/specialties")
            .handle(this::specialtyTypes),
        io.vlingo.xoom.http.resource.ResourceBuilder.get("/specialties/{id}")
            .param(String.class)
            .handle(this::specialtyType)
     );
  }

  private String location(final String id) {
    return "/specialties/" + id;
  }

  private Completes<SpecialtyType> resolve(final String id) {
    return stage().actorOf(SpecialtyType.class, stage().addressFactory().from(id), Definition.has(SpecialtyTypeEntity.class, Definition.parameters(id)));
  }

}
