// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.resource;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;
import com.skyharbor.fleetcrew.infrastructure.persistence.AircraftQueries;
import com.skyharbor.fleetcrew.model.aircraft.Aircraft;
import com.skyharbor.fleetcrew.model.aircraft.AircraftState;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class AircraftResourceHandlers {

  public static final int PLAN_ARRIVAL = 0;
  public static final int RECORD_DEPARTURE = 1;
  public static final int AIRCRAFTS = 2;
  public static final int ADAPT_STATE = 3;

  public static final HandlerEntry<Three<Completes<AircraftState>, Stage, AircraftData>> PLAN_ARRIVAL_HANDLER =
          HandlerEntry.of(PLAN_ARRIVAL, ($stage, data) -> Aircraft.planArrival($stage, data.aircraftId, data.carrier, data.flightNumber, data.tailNumber));

  public static final HandlerEntry<Three<Completes<AircraftState>, Aircraft, AircraftData>> RECORD_DEPARTURE_HANDLER =
          HandlerEntry.of(RECORD_DEPARTURE, (aircraft, data) -> aircraft.recordDeparture(data.carrier, data.flightNumber, data.tailNumber, data.gate));

  public static final HandlerEntry<Two<AircraftData, AircraftState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, AircraftData::from);

  public static final HandlerEntry<Two<Completes<Collection<AircraftData>>, AircraftQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(AIRCRAFTS, AircraftQueries::aircrafts);

}