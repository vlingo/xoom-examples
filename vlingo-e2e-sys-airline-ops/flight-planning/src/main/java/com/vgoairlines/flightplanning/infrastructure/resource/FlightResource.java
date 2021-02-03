// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.resource;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import com.vgoairlines.flightplanning.infrastructure.persistence.FlightQueries;
import com.vgoairlines.flightplanning.infrastructure.persistence.FlightQueriesActor;
import com.vgoairlines.flightplanning.model.flight.Flight;
import com.vgoairlines.flightplanning.model.flight.FlightEntity;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.xoom.annotation.autodispatch.*;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/flight-plannings", handlers=FlightResourceHandlers.class)
@Queries(protocol = FlightQueries.class, actor = FlightQueriesActor.class)
@Model(protocol = Flight.class, actor = FlightEntity.class, data = FlightData.class)
public interface FlightResource {

  @Route(method = POST, handler = FlightResourceHandlers.SCHEDULE)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> schedule(@Body final FlightData data);

  @Route(method = PATCH, path = "/{id}/schedule", handler = FlightResourceHandlers.RESCHEDULE)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> reschedule(@Id final String id, @Body final FlightData data);

  @Route(method = PATCH, path = "/{id}/", handler = FlightResourceHandlers.CANCEL)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> cancel(@Id final String id, @Body final FlightData data);

  @Route(method = GET, handler = FlightResourceHandlers.FLIGHTS)
  Completes<Response> flights();

}