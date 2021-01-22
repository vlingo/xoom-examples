package com.vgoairlines.inventory.infrastructure.exchange;

import io.vlingo.lattice.exchange.ExchangeMapper;
import io.vlingo.common.serialization.JsonSerialization;

import com.vgoairlines.inventory.infrastructure.AircraftData;

public class AircraftDataMapper implements ExchangeMapper<AircraftData,String> {

  @Override
  public String localToExternal(final AircraftData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public AircraftData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, AircraftData.class);
  }
}