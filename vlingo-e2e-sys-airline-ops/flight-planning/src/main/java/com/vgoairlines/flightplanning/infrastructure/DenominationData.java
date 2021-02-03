// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure;

public class DenominationData {

  public final String model;
  public final String serialNumber;
  public final String tailNumber;

  public DenominationData(final String model,
                          final String serialNumber,
                          final String tailNumber) {
    this.model = model;
    this.serialNumber = serialNumber;
    this.tailNumber = tailNumber;
  }
}
