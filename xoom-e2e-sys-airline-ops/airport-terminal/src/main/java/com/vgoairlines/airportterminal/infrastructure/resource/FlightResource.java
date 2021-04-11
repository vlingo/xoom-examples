// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.resource;

import com.vgoairlines.airportterminal.infrastructure.FlightData;
import com.vgoairlines.airportterminal.infrastructure.persistence.FlightQueries;
import com.vgoairlines.airportterminal.infrastructure.persistence.FlightQueriesActor;
import com.vgoairlines.airportterminal.model.flight.Flight;
import com.vgoairlines.airportterminal.model.flight.FlightEntity;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.turbo.annotation.autodispatch.*;

import static io.vlingo.xoom.http.Method.*;

@AutoDispatch(path="/flights", handlers=FlightResourceHandlers.class)
@Queries(protocol = FlightQueries.class, actor = FlightQueriesActor.class)
@Model(protocol = Flight.class, actor = FlightEntity.class, data = FlightData.class)
public interface FlightResource {

  @Route(method = POST, handler = FlightResourceHandlers.OPEN_GATE)
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