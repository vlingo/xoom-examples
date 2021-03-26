package io.vlingo.developers.petclinic.infrastructure.resource;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.developers.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.developers.petclinic.infrastructure.persistence.SpecialtyTypeQueries;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyType;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeEntity;
import io.vlingo.http.Response;
import io.vlingo.http.resource.DynamicResourceHandler;
import io.vlingo.http.resource.Resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.resource;

/**
 * See <a href="https://docs.vlingo.io/vlingo-xoom/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
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
      .otherwise(arg -> Response.of(NotFound, location()))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }

  public Completes<Response> specialtyTypes() {
    return $queries.specialtyTypes()
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  public Completes<Response> specialtyType(String id) {
    return $queries.specialtyTypeOf(id)
            .andThenTo(data -> Completes.withSuccess(Response.of(Ok, serialized(data))))
            .otherwise(arg -> Response.of(NotFound, location()))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }

  @Override
  public Resource<?> routes() {
     return resource("SpecialtyTypeResource",
        io.vlingo.http.resource.ResourceBuilder.post("/specialties/{id}")
            .param(String.class)
            .body(SpecialtyTypeData.class)
            .handle(this::rename),
        io.vlingo.http.resource.ResourceBuilder.post("/specialties")
            .body(SpecialtyTypeData.class)
            .handle(this::offer),
        io.vlingo.http.resource.ResourceBuilder.get("/specialties")
            .handle(this::specialtyTypes),
        io.vlingo.http.resource.ResourceBuilder.get("/specialties/{id}")
            .param(String.class)
            .handle(this::specialtyType)
     );
  }

  private String location() {
    return location("");
  }

  private String location(final String id) {
    return "/specialties/" + id;
  }

  private Completes<SpecialtyType> resolve(final String id) {
    return stage().actorOf(SpecialtyType.class, stage().addressFactory().from(id), Definition.has(SpecialtyTypeEntity.class, Definition.parameters(id)));
  }

}
