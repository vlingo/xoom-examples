package com.skyharbor.airtrafficcontrol.infrastructure.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.annotation.autodispatch.HandlerEntry;

import com.skyharbor.airtrafficcontrol.model.flight.FlightState;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.FlightQueries;
import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;
import com.skyharbor.airtrafficcontrol.model.flight.Flight;
import java.util.Collection;

public class FlightResourceHandlers {

  public static final int DEPART_GATE = 0;
  public static final int FLIGHTS = 1;
  public static final int ADAPT_STATE = 2;

  public static final HandlerEntry<Three<Completes<FlightState>, Stage, FlightData>> DEPART_GATE_HANDLER =
          HandlerEntry.of(DEPART_GATE, ($stage, data) -> Flight.departGate($stage, data.number, data.tailNumber, data.equipment));

  public static final HandlerEntry<Two<FlightData, FlightState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, FlightData::from);

  public static final HandlerEntry<Two<Completes<Collection<FlightData>>, FlightQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(FLIGHTS, FlightQueries::flights);

}