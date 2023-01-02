// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.model.flight;

public enum FlightStatus {

  DEPARTED_GATE,
  OUTBOUND_TAXING,
  IN_FLIGHT_LINE,
  CLEARED_FOR_TAKE_OFF,
  IN_FLIGHT,
  CLEARED_FOR_LANDING,
  LANDED;

  public boolean isLanded() {
    return equals(LANDED);
  }

  public boolean inFlight() {
    return equals(IN_FLIGHT);
  }

}
