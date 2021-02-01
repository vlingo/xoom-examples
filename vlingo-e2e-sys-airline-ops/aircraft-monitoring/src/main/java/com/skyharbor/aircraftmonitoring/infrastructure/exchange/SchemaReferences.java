// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

public enum SchemaReferences {

  DEPARTED_FROM_GATE("SkyHarborPHX:groundops:com.skyharbor.airtrafficcontrol:FlightDepartedGate:0.0.1"),
  TOOK_OFF("SkyHarborPHX:groundops:com.skyharbor.airtrafficcontrol:FlightTookOff:0.0.1"),
  LANDED("SkyHarborPHX:groundops:com.skyharbor.airtrafficcontrol:FlightLanded:0.0.1");

  private final String value;

  SchemaReferences(final String value) {
    this.value = value;
  }

  public boolean match(final String reference) {
    return this.value.equalsIgnoreCase(reference);
  }
}
