// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.exchange.ExchangeMapper;

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