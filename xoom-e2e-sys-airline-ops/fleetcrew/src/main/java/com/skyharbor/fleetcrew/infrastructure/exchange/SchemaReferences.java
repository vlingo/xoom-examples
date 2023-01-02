// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.exchange;

public enum SchemaReferences {

  FLIGHT_LANDED("SkyHarborPHX:groundops:com.skyharbor.airtrafficcontrol:FlightLanded:1.0.0");

  public final String value;

  SchemaReferences(final String value) {
    this.value = value;
  }

  public boolean match(final String schemaName) {
    return this.value.equals(schemaName);
  }
}