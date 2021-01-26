package com.vgoairlines.flightplanning.infrastructure.exchange;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;

public class FlightDataMapper implements ExchangeMapper<FlightData,String> {

  @Override
  public String localToExternal(final FlightData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public FlightData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, FlightData.class);
  }
}