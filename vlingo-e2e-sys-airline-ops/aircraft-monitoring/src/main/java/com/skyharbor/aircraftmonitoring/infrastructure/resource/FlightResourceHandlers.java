// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.resource;

import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;
import com.skyharbor.aircraftmonitoring.infrastructure.persistence.FlightQueries;
import com.skyharbor.aircraftmonitoring.model.flight.Flight;
import com.skyharbor.aircraftmonitoring.model.flight.FlightState;
import com.skyharbor.aircraftmonitoring.model.flight.Location;
import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class FlightResourceHandlers {

  public static final int REPORT_LOCATION = 0;
  public static final int FLIGHTS = 1;
  public static final int ADAPT_STATE = 2;

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> REPORT_LOCATION_HANDLER =
          HandlerEntry.of(REPORT_LOCATION, (flight, data) ->
            flight.reportLocation(Location.at(data.location.altitude, data.location.latitude, data.location.longitude)));

  public static final HandlerEntry<Two<FlightData, FlightState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, FlightData::from);

  public static final HandlerEntry<Two<Completes<Collection<FlightData>>, FlightQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(FLIGHTS, FlightQueries::flights);

}