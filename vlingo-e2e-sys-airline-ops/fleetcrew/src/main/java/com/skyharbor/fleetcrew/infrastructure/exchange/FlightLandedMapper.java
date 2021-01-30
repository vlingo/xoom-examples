package com.skyharbor.fleetcrew.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import com.skyharbor.fleetcrew.infrastructure.AircraftData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;

public class FlightLandedMapper implements ExchangeMapper<FlightLanded,String> {

  @Override
  public String localToExternal(final FlightLanded local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public FlightLanded externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, FlightLanded.class);
  }
}