// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.resource;

import com.vgoairlines.airportterminal.infrastructure.FlightData;
import com.vgoairlines.airportterminal.infrastructure.persistence.FlightQueries;
import com.vgoairlines.airportterminal.model.flight.*;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class FlightResourceHandlers {

  public static final int OPEN_GATE = 0;
  public static final int START_BOARDING = 1;
  public static final int COMPLETE_BOARDING = 2;
  public static final int DEPART = 3;
  public static final int CLOSE_GATE = 4;
  public static final int FLIGHTS = 5;
  public static final int ADAPT_STATE = 6;

  public static final HandlerEntry<Three<Completes<FlightState>, Stage, FlightData>> OPEN_GATE_HANDLER =
          HandlerEntry.of(OPEN_GATE, ($stage, data) -> {
            final GateAssignment gateAssignment =
                    GateAssignment.with(data.gateAssignment.terminal, data.gateAssignment.number);

            final Equipment equipment =
                    Equipment.of(data.equipment.carrier, data.equipment.tailNumber);

            final Schedule schedule =
                    Schedule.on(data.schedule.scheduledDeparture, data.schedule.scheduledArrival);

            return Flight.openGate($stage, data.number, data.aircraftId, gateAssignment, equipment, schedule);
          });

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> START_BOARDING_HANDLER =
          HandlerEntry.of(START_BOARDING, (flight, data) -> flight.startBoarding());

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> COMPLETE_BOARDING_HANDLER =
          HandlerEntry.of(COMPLETE_BOARDING, (flight, data) -> flight.endBoarding());

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> DEPART_HANDLER =
          HandlerEntry.of(DEPART, (flight, data) -> {
            return flight.depart(data.schedule.departureStatus.actual);
          });

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> CLOSE_GATE_HANDLER =
          HandlerEntry.of(CLOSE_GATE, (flight, data) -> flight.closeGate());

  public static final HandlerEntry<Two<FlightData, FlightState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, FlightData::from);

  public static final HandlerEntry<Two<Completes<Collection<FlightData>>, FlightQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(FLIGHTS, FlightQueries::flights);

}