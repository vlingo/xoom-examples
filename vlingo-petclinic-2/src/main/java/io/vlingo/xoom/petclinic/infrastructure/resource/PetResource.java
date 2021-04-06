package io.vlingo.xoom.petclinic.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import io.vlingo.xoom.petclinic.infrastructure.persistence.PetQueries;
import io.vlingo.xoom.petclinic.infrastructure.persistence.PetQueriesActor;
import io.vlingo.xoom.petclinic.model.pet.Pet;
import io.vlingo.xoom.petclinic.model.pet.PetEntity;
import io.vlingo.xoom.petclinic.infrastructure.PetData;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/", handlers=PetResourceHandlers.class)
@Queries(protocol = PetQueries.class, actor = PetQueriesActor.class)
@Model(protocol = Pet.class, actor = PetEntity.class, data = PetData.class)
public interface PetResource {

  @Route(method = POST, path = "pets", handler = PetResourceHandlers.REGISTER)
  @ResponseAdapter(handler = PetResourceHandlers.ADAPT_STATE)
  Completes<Response> register(@Body final PetData data);

  @Route(method = PATCH, path = "pets/{id}/name", handler = PetResourceHandlers.CHANGE_NAME)
  @ResponseAdapter(handler = PetResourceHandlers.ADAPT_STATE)
  Completes<Response> changeName(@Id final String id, @Body final PetData data);

  @Route(method = PATCH, path = "pets/{id}/birth", handler = PetResourceHandlers.RECORD_BIRTH)
  @ResponseAdapter(handler = PetResourceHandlers.ADAPT_STATE)
  Completes<Response> recordBirth(@Id final String id, @Body final PetData data);

  @Route(method = PATCH, path = "pets/{id}/death", handler = PetResourceHandlers.RECORD_DEATH)
  @ResponseAdapter(handler = PetResourceHandlers.ADAPT_STATE)
  Completes<Response> recordDeath(@Id final String id, @Body final PetData data);

  @Route(method = PATCH, path = "pets/{id}/kind", handler = PetResourceHandlers.CORRECT_KIND)
  @ResponseAdapter(handler = PetResourceHandlers.ADAPT_STATE)
  Completes<Response> correctKind(@Id final String id, @Body final PetData data);

  @Route(method = PATCH, path = "pets/{id}/owner", handler = PetResourceHandlers.CHANGE_OWNER)
  @ResponseAdapter(handler = PetResourceHandlers.ADAPT_STATE)
  Completes<Response> changeOwner(@Id final String id, @Body final PetData data);

  @Route(method = GET, handler = PetResourceHandlers.PETS)
  Completes<Response> pets();

}