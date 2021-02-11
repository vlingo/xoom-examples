// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.model.flight;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

public class AirportRetriever {

  public static String airportCodeForDeparture(final Date departureOccurredOn) {
    return airportCode(departureOccurredOn, time -> {
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(departureOccurredOn);
      return calendar.get(Calendar.DAY_OF_MONTH) %2 == 0;
    });
  }

  public static String airportCodeForArrival(final Date estimatedArrival) {
    return airportCode(estimatedArrival, time -> {
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(estimatedArrival);
      return calendar.get(Calendar.DAY_OF_MONTH) % 2 != 0;
    });
  }

  private static String airportCode(final Date time, final Predicate<Date> condition) {
    if(condition.test(time)) {
      return "PHX";
    }
    return "LAX";
  }
}
