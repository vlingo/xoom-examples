// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.aircraft;

public class Denomination {

  public final String model;
  public final String serialNumber;
  public final String tailNumber;

  public static Denomination from(final String model, final String serialNumber, final String tailNumber) {
    return new Denomination(model, serialNumber, tailNumber);
  }

  private Denomination(final String model, final String serialNumber, final String tailNumber) {
    this.model = model;
    this.serialNumber = serialNumber;
    this.tailNumber = tailNumber;
  }

}
