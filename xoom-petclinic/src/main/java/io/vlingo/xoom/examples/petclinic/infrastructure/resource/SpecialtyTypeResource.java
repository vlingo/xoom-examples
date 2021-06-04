package io.vlingo.xoom.examples.petclinic.infrastructure.resource;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.*;
import io.vlingo.xoom.http.Response;

import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyType;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeEntity;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.SpecialtyTypeQueries;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.SpecialtyTypeQueriesActor;

import static io.vlingo.xoom.http.Method.*;

@AutoDispatch(path="/", handlers=SpecialtyTypeResourceHandlers.class)
@Queries(protocol = SpecialtyTypeQueries.class, actor = SpecialtyTypeQueriesActor.class)
@Model(protocol = SpecialtyType.class, actor = SpecialtyTypeEntity.class, data = SpecialtyTypeData.class)
public interface SpecialtyTypeResource {

  @Route(method = POST, path = "specialties", handler = SpecialtyTypeResourceHandlers.OFFER)
  @ResponseAdapter(handler = SpecialtyTypeResourceHandlers.ADAPT_STATE)
  Completes<Response> offer(@Body final SpecialtyTypeData data);

  @Route(method = PATCH, path = "specialties/{id}/name", handler = SpecialtyTypeResourceHandlers.RENAME)
  @ResponseAdapter(handler = SpecialtyTypeResourceHandlers.ADAPT_STATE)
  Completes<Response> rename(@Id final String id, @Body final SpecialtyTypeData data);

  @Route(method = GET, path = "specialties", handler = SpecialtyTypeResourceHandlers.SPECIALTY_TYPES)
  Completes<Response> specialtyTypes();

}