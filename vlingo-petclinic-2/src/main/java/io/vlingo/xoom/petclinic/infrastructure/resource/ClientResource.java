package io.vlingo.xoom.petclinic.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import io.vlingo.xoom.petclinic.infrastructure.persistence.ClientQueriesActor;
import io.vlingo.xoom.petclinic.infrastructure.persistence.ClientQueries;
import io.vlingo.xoom.petclinic.model.client.ClientEntity;
import io.vlingo.xoom.petclinic.model.client.Client;
import io.vlingo.xoom.petclinic.infrastructure.ClientData;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/", handlers=ClientResourceHandlers.class)
@Queries(protocol = ClientQueries.class, actor = ClientQueriesActor.class)
@Model(protocol = Client.class, actor = ClientEntity.class, data = ClientData.class)
public interface ClientResource {

  @Route(method = POST, path = "clients", handler = ClientResourceHandlers.REGISTER)
  @ResponseAdapter(handler = ClientResourceHandlers.ADAPT_STATE)
  Completes<Response> register(@Body final ClientData data);

  @Route(method = PATCH, path = "clients/{id}/contact", handler = ClientResourceHandlers.CHANGE_CONTACT_INFORMATION)
  @ResponseAdapter(handler = ClientResourceHandlers.ADAPT_STATE)
  Completes<Response> changeContactInformation(@Id final String id, @Body final ClientData data);

  @Route(method = PATCH, path = "clients/{id}/name", handler = ClientResourceHandlers.CHANGE_NAME)
  @ResponseAdapter(handler = ClientResourceHandlers.ADAPT_STATE)
  Completes<Response> changeName(@Id final String id, @Body final ClientData data);

  @Route(method = GET, handler = ClientResourceHandlers.CLIENTS)
  Completes<Response> clients();

}