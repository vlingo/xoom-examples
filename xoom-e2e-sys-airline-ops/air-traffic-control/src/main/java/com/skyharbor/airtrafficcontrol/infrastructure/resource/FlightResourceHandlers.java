// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.resource;

import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.FlightQueries;
import com.skyharbor.airtrafficcontrol.model.flight.Flight;
import com.skyharbor.airtrafficcontrol.model.flight.FlightState;
import com.skyharbor.airtrafficcontrol.model.flight.FlightStatus;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class FlightResourceHandlers {

  public static final int DEPART_GATE = 0;
  public static final int FLIGHTS = 1;
  public static final int ADAPT_STATE = 2;
  public static final int CHANGE_STATUS = 3;

  public static final HandlerEntry<Three<Completes<FlightState>, Stage, FlightData>> DEPART_GATE_HANDLER =
          HandlerEntry.of(DEPART_GATE, ($stage, data) -> Flight.departFromGate($stage, data.aircraftId, data.number, data.tailNumber, data.equipment));

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> CHANGE_STATUS_HANDLER =
          HandlerEntry.of(CHANGE_STATUS, (flight, data) -> {
            if(FlightStatus.valueOf(data.status).isLanded()) {
              return flight.land(data.number);
            } else if(FlightStatus.valueOf(data.status).inFlight()) {
              return flight.takeOff(data.number);
            }
            throw new UnsupportedOperationException("Unable to change Flight Status to " + data.status);
          });

  public static final HandlerEntry<Two<FlightData, FlightState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, FlightData::from);

  public static final HandlerEntry<Two<Completes<Collection<FlightData>>, FlightQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(FLIGHTS, FlightQueries::flights);

}