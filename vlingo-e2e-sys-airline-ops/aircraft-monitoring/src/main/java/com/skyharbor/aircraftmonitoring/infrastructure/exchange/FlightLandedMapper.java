// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;

public class FlightLandedMapper implements ExchangeMapper<FlightLanded, String> {

  @Override
  public String localToExternal(final FlightLanded flightLanded) {
    return JsonSerialization.serialized(flightLanded);
  }

  @Override
  public FlightLanded externalToLocal(final String payload) {
    return JsonSerialization.deserialized(payload, FlightLanded.class);
  }
}
