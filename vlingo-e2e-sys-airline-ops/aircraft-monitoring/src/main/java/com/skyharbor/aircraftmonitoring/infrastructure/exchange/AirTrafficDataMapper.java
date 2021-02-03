// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.ExchangeMapper;

public class AirTrafficDataMapper implements ExchangeMapper<AirTrafficDataMapper.AirTrafficData, String> {

  @Override
  public String localToExternal(final AirTrafficData local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public AirTrafficData externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, AirTrafficData.class);
  }

  static class AirTrafficData {
    public final String id;
    public final String aircraftId;
    public final String number;
    public final String tailNumber;
    public final String equipment;

    AirTrafficData(final String id,
                   final String aircraftId,
                   final String number,
                   final String tailNumber,
                   final String equipment) {
      this.id = id;
      this.aircraftId = aircraftId;
      this.number = number;
      this.tailNumber = tailNumber;
      this.equipment = equipment;
    }

    public static AirTrafficData empty() {
      return new AirTrafficData("", "", "", "", "");
    }
  }
}
