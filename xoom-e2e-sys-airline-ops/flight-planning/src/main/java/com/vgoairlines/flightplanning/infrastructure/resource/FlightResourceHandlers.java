// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.resource;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import com.vgoairlines.flightplanning.infrastructure.persistence.FlightQueries;
import com.vgoairlines.flightplanning.model.flight.*;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class FlightResourceHandlers {

  public static final int SCHEDULE = 0;
  public static final int RESCHEDULE = 1;
  public static final int CANCEL = 2;
  public static final int FLIGHTS = 3;
  public static final int ADAPT_STATE = 4;

  public static final HandlerEntry<Three<Completes<FlightState>, Stage, FlightData>> SCHEDULE_HANDLER =
          HandlerEntry.of(SCHEDULE, ($stage, data) -> {
            final Airport departureAirport =
                    Airport.with(data.schedule.departure.airport.code);

            final Airport arrivalAirport =
                    Airport.with(data.schedule.arrival.airport.code);

            final Schedule schedule =
                    Schedule.with(Departure.on(departureAirport, data.schedule.departure.plannedFor),
                            Arrival.on(arrivalAirport, data.schedule.arrival.plannedFor));

            return Flight.schedule($stage, AircraftId.of(data.aircraftId), schedule);
          });

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> RESCHEDULE_HANDLER =
          HandlerEntry.of(RESCHEDULE, (flight, data) -> {
              final Airport departureAirport =
                      Airport.with(data.schedule.departure.airport.code);

              final Airport arrivalAirport =
                      Airport.with(data.schedule.arrival.airport.code);

              final Schedule schedule =
                      Schedule.with(Departure.on(departureAirport, data.schedule.departure.plannedFor),
                              Arrival.on(arrivalAirport, data.schedule.arrival.plannedFor));

              return flight.reschedule(schedule);
          });

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> CANCEL_HANDLER =
          HandlerEntry.of(CANCEL, (flight, data) -> flight.cancel());

  public static final HandlerEntry<Two<FlightData, FlightState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, FlightData::from);

  public static final HandlerEntry<Two<Completes<Collection<FlightData>>, FlightQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(FLIGHTS, FlightQueries::flights);

}