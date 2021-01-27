package com.skyharbor.airtrafficcontrol.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import com.skyharbor.airtrafficcontrol.model.flight.FlightEntity;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.FlightQueries;
import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.FlightQueriesActor;
import com.skyharbor.airtrafficcontrol.model.flight.Flight;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/flights", handlers=FlightResourceHandlers.class)
@Queries(protocol = FlightQueries.class, actor = FlightQueriesActor.class)
@Model(protocol = Flight.class, actor = FlightEntity.class, data = FlightData.class)
public interface FlightResource {

  @Route(method = POST, handler = FlightResourceHandlers.DEPART_GATE)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> departGate(@Body final FlightData data);

  @Route(method = GET, handler = FlightResourceHandlers.FLIGHTS)
  Completes<Response> flights();

}