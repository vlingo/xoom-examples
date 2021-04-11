// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.exchange;

import com.vgoairlines.inventory.event.AircraftConsigned;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.exchange.ExchangeMapper;

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