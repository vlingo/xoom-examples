// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightTookOff;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;

public class FlightTookOffMapper implements ExchangeMapper<FlightTookOff, String> {

  @Override
  public String localToExternal(final FlightTookOff flightLanded) {
    return JsonSerialization.serialized(flightLanded);
  }

  @Override
  public FlightTookOff externalToLocal(final String payload) {
    return JsonSerialization.deserialized(payload, FlightTookOff.class);
  }
}
