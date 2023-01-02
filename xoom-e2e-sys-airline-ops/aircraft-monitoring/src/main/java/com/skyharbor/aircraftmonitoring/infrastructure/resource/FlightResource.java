// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.resource;

import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;
import com.skyharbor.aircraftmonitoring.infrastructure.persistence.FlightQueries;
import com.skyharbor.aircraftmonitoring.infrastructure.persistence.FlightQueriesActor;
import com.skyharbor.aircraftmonitoring.model.flight.Flight;
import com.skyharbor.aircraftmonitoring.model.flight.FlightEntity;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.turbo.annotation.autodispatch.*;

import static io.vlingo.xoom.http.Method.GET;
import static io.vlingo.xoom.http.Method.PATCH;

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