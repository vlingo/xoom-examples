// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightDepartedGate;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;

public class FlightDepartedMapper implements ExchangeMapper<FlightDepartedGate, String> {

  @Override
  public String localToExternal(final FlightDepartedGate local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public FlightDepartedGate externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, FlightDepartedGate.class);
  }
}
