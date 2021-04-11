// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.resource;

import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.FlightQueries;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.FlightQueriesActor;
import com.skyharbor.airtrafficcontrol.model.flight.Flight;
import com.skyharbor.airtrafficcontrol.model.flight.FlightEntity;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.turbo.annotation.autodispatch.*;

import static io.vlingo.xoom.http.Method.*;

@AutoDispatch(path="/flights", handlers=FlightResourceHandlers.class)
@Queries(protocol = FlightQueries.class, actor = FlightQueriesActor.class)
@Model(protocol = Flight.class, actor = FlightEntity.class, data = FlightData.class)
public interface FlightResource {

  @Route(method = POST, handler = FlightResourceHandlers.DEPART_GATE)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> departGate(@Body final FlightData data);

  @Route(method = PATCH, path = "/{id}/status", handler = FlightResourceHandlers.CHANGE_STATUS)
  @ResponseAdapter(handler = FlightResourceHandlers.ADAPT_STATE)
  Completes<Response> changeStatus(@Id final String id, @Body final FlightData data);

  @Route(method = GET, handler = FlightResourceHandlers.FLIGHTS)
  Completes<Response> flights();

}