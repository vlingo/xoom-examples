package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import io.vlingo.lattice.exchange.ExchangeMapper;
import io.vlingo.common.serialization.JsonSerialization;

import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;

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