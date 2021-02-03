// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

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

  @Route(method = POST, handler = AircraftResourceHandlers.CONSIGN)
  @ResponseAdapter(handler = AircraftResourceHandlers.ADAPT_STATE)
  Completes<Response> consign(@Body final AircraftData data);

  @Route(method = GET, handler = AircraftResourceHandlers.AIRCRAFTS)
  Completes<Response> aircrafts();

}