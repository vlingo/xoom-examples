// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

import java.util.Date;

public class EstimatedArrival {

  public final String airportCode;
  public final Date time;

  public static EstimatedArrival resolve(){
    return new EstimatedArrival(new Date());
  }

  private EstimatedArrival(final Date time) {
    this.time = time;
    this.airportCode = AirportRetriever.airportCodeForArrival(time);
  }

}
