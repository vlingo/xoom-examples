// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.resource;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;
import com.skyharbor.fleetcrew.infrastructure.persistence.AircraftQueries;
import com.skyharbor.fleetcrew.infrastructure.persistence.AircraftQueriesActor;
import com.skyharbor.fleetcrew.model.aircraft.Aircraft;
import com.skyharbor.fleetcrew.model.aircraft.AircraftEntity;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.turbo.annotation.autodispatch.*;

import static io.vlingo.xoom.http.Method.*;

@AutoDispatch(path="/fleetcrew/aircrafts", handlers=AircraftResourceHandlers.class)
@Queries(protocol = AircraftQueries.class, actor = AircraftQueriesActor.class)
@Model(protocol = Aircraft.class, actor = AircraftEntity.class, data = AircraftData.class)
public interface AircraftResource {

  @Route(method = POST, path = "/", handler = AircraftResourceHandlers.PLAN_ARRIVAL)
  @ResponseAdapter(handler = AircraftResourceHandlers.ADAPT_STATE)
  Completes<Response> planArrival(@Body final AircraftData data);

  @Route(method = PATCH, path = "/{id}/departure", handler = AircraftResourceHandlers.RECORD_DEPARTURE)
  @ResponseAdapter(handler = AircraftResourceHandlers.ADAPT_STATE)
  Completes<Response> recordDeparture(@Id final String id, @Body final AircraftData data);

  @Route(method = GET, handler = AircraftResourceHandlers.AIRCRAFTS)
  Completes<Response> aircrafts();

}