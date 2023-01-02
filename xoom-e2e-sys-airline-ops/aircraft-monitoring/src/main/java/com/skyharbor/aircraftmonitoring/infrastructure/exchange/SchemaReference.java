// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import java.util.stream.Stream;

public enum SchemaReference {

  DEPARTED_FROM_GATE("SkyHarborPHX:groundops:com.skyharbor.airtrafficcontrol:FlightDepartedGate:1.0.0"),
  TOOK_OFF("SkyHarborPHX:groundops:com.skyharbor.airtrafficcontrol:FlightTookOff:1.0.0"),
  LANDED("SkyHarborPHX:groundops:com.skyharbor.airtrafficcontrol:FlightLanded:1.0.0");

  private final String value;

  SchemaReference(final String value) {
    this.value = value;
  }

  public boolean match(final String reference) {
    return this.value.equalsIgnoreCase(reference);
  }

  public static SchemaReference find(final String reference) {
    return Stream.of(values()).filter(ref -> ref.match(reference))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Schema Reference not supported " + reference ));
  }

  public static boolean matchAny(final String reference) {
    return Stream.of(values()).anyMatch(ref -> ref.match(reference));
  }

}
