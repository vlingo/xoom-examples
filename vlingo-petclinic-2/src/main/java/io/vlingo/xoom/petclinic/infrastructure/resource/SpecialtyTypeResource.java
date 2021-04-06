package io.vlingo.xoom.petclinic.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import io.vlingo.xoom.petclinic.model.specialtytype.SpecialtyType;
import io.vlingo.xoom.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.petclinic.model.specialtytype.SpecialtyTypeEntity;
import io.vlingo.xoom.petclinic.infrastructure.persistence.SpecialtyTypeQueries;
import io.vlingo.xoom.petclinic.infrastructure.persistence.SpecialtyTypeQueriesActor;

import static io.vlingo.http.Method.*;

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

  @Route(method = GET, handler = SpecialtyTypeResourceHandlers.SPECIALTY_TYPES)
  Completes<Response> specialtyTypes();

}