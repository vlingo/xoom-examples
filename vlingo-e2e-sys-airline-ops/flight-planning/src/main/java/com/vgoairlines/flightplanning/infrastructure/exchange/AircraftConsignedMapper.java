package com.vgoairlines.flightplanning.infrastructure.exchange;

import com.vgoairlines.inventory.event.AircraftConsigned;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;

public class AircraftConsignedMapper implements ExchangeMapper<AircraftConsigned,String> {

  @Override
  public String localToExternal(final AircraftConsigned local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public AircraftConsigned externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, AircraftConsigned.class);
  }
}