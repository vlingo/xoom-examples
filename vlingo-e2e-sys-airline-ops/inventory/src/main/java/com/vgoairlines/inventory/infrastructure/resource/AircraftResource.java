package com.vgoairlines.inventory.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import com.vgoairlines.inventory.model.aircraft.AircraftEntity;
import com.vgoairlines.inventory.infrastructure.AircraftData;
import com.vgoairlines.inventory.infrastructure.persistence.AircraftQueries;
import com.vgoairlines.inventory.model.aircraft.Aircraft;
import com.vgoairlines.inventory.infrastructure.persistence.AircraftQueriesActor;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/aircrafts", handlers=AircraftResourceHandlers.class)
@Queries(protocol = AircraftQueries.class, actor = AircraftQueriesActor.class)
@Model(protocol = Aircraft.class, actor = AircraftEntity.class, data = AircraftData.class)
public interface AircraftResource {

  @Route(method = POST, path = "/", handler = AircraftResourceHandlers.CONSIGN)
  @ResponseAdapter(handler = AircraftResourceHandlers.ADAPT_STATE)
  Completes<Response> consign(@Body final AircraftData data);

  @Route(method = GET, handler = AircraftResourceHandlers.AIRCRAFTS)
  Completes<Response> aircrafts();

}