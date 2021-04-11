// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;


import java.util.Date;

public class ActualDeparture {

  public final String airportCode;
  public final Date occurredOn;

  public static ActualDeparture resolve() {
    return new ActualDeparture(new Date());
  }

  private ActualDeparture(final Date occurredOn) {
    this.occurredOn = occurredOn;
    this.airportCode = AirportRetriever.airportCodeForDeparture(occurredOn);
  }

}
