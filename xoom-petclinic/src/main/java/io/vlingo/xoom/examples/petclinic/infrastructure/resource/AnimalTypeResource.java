package io.vlingo.xoom.examples.petclinic.infrastructure.resource;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.*;
import io.vlingo.xoom.http.Response;

import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.AnimalTypeQueriesActor;
import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.AnimalTypeQueries;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalType;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeEntity;

import static io.vlingo.xoom.http.Method.*;

@AutoDispatch(path="/", handlers=AnimalTypeResourceHandlers.class)
@Queries(protocol = AnimalTypeQueries.class, actor = AnimalTypeQueriesActor.class)
@Model(protocol = AnimalType.class, actor = AnimalTypeEntity.class, data = AnimalTypeData.class)
public interface AnimalTypeResource {

  @Route(method = POST, path = "animalTypes", handler = AnimalTypeResourceHandlers.OFFER_TREATMENT_FOR)
  @ResponseAdapter(handler = AnimalTypeResourceHandlers.ADAPT_STATE)
  Completes<Response> offerTreatmentFor(@Body final AnimalTypeData data);

  @Route(method = PATCH, path = "animalTypes/{id}/name", handler = AnimalTypeResourceHandlers.RENAME)
  @ResponseAdapter(handler = AnimalTypeResourceHandlers.ADAPT_STATE)
  Completes<Response> rename(@Id final String id, @Body final AnimalTypeData data);

  @Route(method = GET, path = "animalTypes", handler = AnimalTypeResourceHandlers.ANIMAL_TYPES)
  Completes<Response> animalTypes();

}