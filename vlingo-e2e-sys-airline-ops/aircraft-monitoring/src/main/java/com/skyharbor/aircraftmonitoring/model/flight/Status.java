// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

public enum Status {
  DEPARTED_GATE,
  ARRIVED_AT_GATE,
  IN_FLIGHT,
  LANDED;

  public boolean isLanded() {
    return equals(LANDED);
  }

  public boolean inFlight() {
    return equals(IN_FLIGHT);
  }
}
