package io.vlingo.xoom.petclinic.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import io.vlingo.xoom.petclinic.model.veterinarian.VeterinarianEntity;
import io.vlingo.xoom.petclinic.infrastructure.persistence.VeterinarianQueriesActor;
import io.vlingo.xoom.petclinic.infrastructure.persistence.VeterinarianQueries;
import io.vlingo.xoom.petclinic.model.veterinarian.Veterinarian;
import io.vlingo.xoom.petclinic.infrastructure.VeterinarianData;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/", handlers=VeterinarianResourceHandlers.class)
@Queries(protocol = VeterinarianQueries.class, actor = VeterinarianQueriesActor.class)
@Model(protocol = Veterinarian.class, actor = VeterinarianEntity.class, data = VeterinarianData.class)
public interface VeterinarianResource {

  @Route(method = POST, path = "veterinarians", handler = VeterinarianResourceHandlers.REGISTER)
  @ResponseAdapter(handler = VeterinarianResourceHandlers.ADAPT_STATE)
  Completes<Response> register(@Body final VeterinarianData data);

  @Route(method = PATCH, path = "veterinarians/{id}/contact", handler = VeterinarianResourceHandlers.CHANGE_CONTACT_INFORMATION)
  @ResponseAdapter(handler = VeterinarianResourceHandlers.ADAPT_STATE)
  Completes<Response> changeContactInformation(@Id final String id, @Body final VeterinarianData data);

  @Route(method = PATCH, path = "veterinarians/{id}/name", handler = VeterinarianResourceHandlers.CHANGE_NAME)
  @ResponseAdapter(handler = VeterinarianResourceHandlers.ADAPT_STATE)
  Completes<Response> changeName(@Id final String id, @Body final VeterinarianData data);

  @Route(method = PATCH, path = "veterinarians/{id}/specialty", handler = VeterinarianResourceHandlers.SPECIALIZES_IN)
  @ResponseAdapter(handler = VeterinarianResourceHandlers.ADAPT_STATE)
  Completes<Response> specializesIn(@Id final String id, @Body final VeterinarianData data);

  @Route(method = GET, handler = VeterinarianResourceHandlers.VETERINARIANS)
  Completes<Response> veterinarians();

}