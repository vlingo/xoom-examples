// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure;

import java.util.Date;

public class ActualDepartureData {

  public final String airportCode;
  public final Date occurredOn;

  public ActualDepartureData(final String airportCode, final Date occurredOn) {
    this.occurredOn = occurredOn;
    this.airportCode = airportCode;
  }

}
