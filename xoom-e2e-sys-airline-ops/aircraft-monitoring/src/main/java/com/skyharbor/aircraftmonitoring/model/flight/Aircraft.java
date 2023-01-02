// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

public class Aircraft {

  public final String aircraftId;
  public final String tailNumber;
  public final String carrier;

  public static Aircraft of(final String aircraftId, final String tailNumber, final String carrier) {
    return new Aircraft(aircraftId, tailNumber, carrier);
  }

  private Aircraft(final String aircraftId, final String tailNumber, final String carrier) {
    this.aircraftId = aircraftId;
    this.tailNumber = tailNumber;
    this.carrier = carrier;
  }
}
