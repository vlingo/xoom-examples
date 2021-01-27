package com.skyharbor.aircraftmonitoring.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import com.skyharbor.aircraftmonitoring.infrastructure.persistence.FlightQueriesActor;
import com.skyharbor.aircraftmonitoring.infrastructure.persistence.FlightQueries;
import com.skyharbor.aircraftmonitoring.model.flight.FlightEntity;
import com.skyharbor.aircraftmonitoring.model.flight.Flight;
import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/flights", handlers=FlightResourceHandlers.class)
@Queries(protocol = FlightQueries.class, actor = FlightQueriesActor.class)
@Model(protocol = Flight.class, actor = FlightEntity.class, data = FlightData.class)
public interface FlightResource {

  @Route(method = PATCH, path = "/{id}/location", handler = FlightResourceHandlers.REPORT_LOCATION)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> reportLocation(@Id final String id, @Body final FlightData data);

  @Route(method = GET, handler = FlightResourceHandlers.FLIGHTS)
  Completes<Response> flights();

}