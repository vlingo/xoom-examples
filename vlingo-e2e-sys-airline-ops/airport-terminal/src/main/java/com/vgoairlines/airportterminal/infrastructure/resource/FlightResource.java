package com.vgoairlines.airportterminal.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import com.vgoairlines.airportterminal.infrastructure.persistence.FlightQueries;
import com.vgoairlines.airportterminal.model.flight.FlightEntity;
import com.vgoairlines.airportterminal.model.flight.Flight;
import com.vgoairlines.airportterminal.infrastructure.FlightData;
import com.vgoairlines.airportterminal.infrastructure.persistence.FlightQueriesActor;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/flights", handlers=FlightResourceHandlers.class)
@Queries(protocol = FlightQueries.class, actor = FlightQueriesActor.class)
@Model(protocol = Flight.class, actor = FlightEntity.class, data = FlightData.class)
public interface FlightResource {

  @Route(method = POST, path = "/", handler = FlightResourceHandlers.OPEN_GATE)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> openGate(@Body final FlightData data);

  @Route(method = PATCH, path = "/{id}/boarding", handler = FlightResourceHandlers.START_BOARDING)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> startBoarding(@Id final String id, @Body final FlightData data);

  @Route(method = PUT, path = "/{id}/boarding", handler = FlightResourceHandlers.COMPLETE_BOARDING)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> completeBoarding(@Id final String id, @Body final FlightData data);

  @Route(method = PATCH, path = "/{id}/schedule", handler = FlightResourceHandlers.DEPART)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> depart(@Id final String id, @Body final FlightData data);

  @Route(method = PUT, path = "/{id}/", handler = FlightResourceHandlers.CLOSE_GATE)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> closeGate(@Id final String id, @Body final FlightData data);

  @Route(method = GET, handler = FlightResourceHandlers.FLIGHTS)
  Completes<Response> flights();

}