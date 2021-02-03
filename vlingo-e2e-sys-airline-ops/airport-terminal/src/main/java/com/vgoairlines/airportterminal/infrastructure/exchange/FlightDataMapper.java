// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure.exchange;

import io.vlingo.lattice.exchange.ExchangeMapper;
import io.vlingo.common.serialization.JsonSerialization;

import com.vgoairlines.airportterminal.infrastructure.FlightData;

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