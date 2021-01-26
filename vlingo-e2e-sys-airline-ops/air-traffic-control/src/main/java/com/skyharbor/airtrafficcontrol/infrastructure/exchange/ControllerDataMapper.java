package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import io.vlingo.lattice.exchange.ExchangeMapper;
import io.vlingo.common.serialization.JsonSerialization;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;

public class ControllerDataMapper implements ExchangeMapper<ControllerData,String> {

  @Override
  public String localToExternal(final ControllerData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public ControllerData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, ControllerData.class);
  }
}