package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.vgoairlines.airportterminal.event.FlightDeparted;
import io.vlingo.lattice.exchange.ExchangeMapper;
import io.vlingo.common.serialization.JsonSerialization;

import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;

public class FlightDepartedMapper implements ExchangeMapper<FlightDeparted, String> {

  @Override
  public String localToExternal(final FlightDeparted local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public FlightDeparted externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, FlightDeparted.class);
  }
}