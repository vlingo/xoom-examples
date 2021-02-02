// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

public enum SchemaReferences {

  FLIGHT_DEPARTED("VgoAirlines:AirportTerminal:com.vgoairlines.airportterminal:FlightDeparted:1.0.0");

  private final String value;

  SchemaReferences(final String value) {
    this.value = value;
  }

  public boolean match(final String reference) {
    return value.equalsIgnoreCase(reference);
  }
}
