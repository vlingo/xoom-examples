// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightLanded;
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