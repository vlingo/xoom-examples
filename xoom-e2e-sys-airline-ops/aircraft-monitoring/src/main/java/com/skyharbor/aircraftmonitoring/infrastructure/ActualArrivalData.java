// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure;

import java.util.Date;

public class ActualArrivalData {

  public final String airportCode;
  public final Date occurredOn;

  public static ActualArrivalData at(final Date occurredOn) {
    return new ActualArrivalData(occurredOn);
  }

  public ActualArrivalData(final Date occurredOn) {
    this.airportCode = null;
    this.occurredOn = occurredOn;
  }

}
