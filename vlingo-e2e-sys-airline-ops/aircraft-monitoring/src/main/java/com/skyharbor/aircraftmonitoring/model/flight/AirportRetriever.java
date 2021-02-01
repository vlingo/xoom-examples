// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

import java.time.LocalDateTime;
import java.util.function.Predicate;

public class AirportRetriever {

  public static String airportCodeForDeparture(final LocalDateTime departureOccurredOn) {
    return airportCode(departureOccurredOn, time -> time.getDayOfMonth() % 2 == 0);
  }

  public static String airportCodeForArrival(final LocalDateTime estimatedArrival) {
    return airportCode(estimatedArrival, time -> time.getDayOfMonth() % 2 != 0);
  }

  private static String airportCode(final LocalDateTime time, final Predicate<LocalDateTime> condition) {
    if(condition.test(time)) {
      return "PHX";
    }
    return "LAX";
  }
}
