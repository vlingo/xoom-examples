// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

import java.time.LocalDateTime;

public class ActualArrival {

  public final String airportCode;
  public final LocalDateTime occurredOn;

  public static ActualArrival at(final LocalDateTime occurredOn) {
    return new ActualArrival(occurredOn);
  }

  private ActualArrival(final LocalDateTime occurredOn) {
    this.airportCode = null;
    this.occurredOn = occurredOn;
  }

}
