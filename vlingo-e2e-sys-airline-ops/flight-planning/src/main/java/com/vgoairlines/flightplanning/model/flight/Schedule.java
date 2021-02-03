// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.flight;

public class Schedule {

    public final Departure departure;
    public final Arrival arrival;

    public static Schedule with(final Departure departure, final Arrival arrival) {
        return new Schedule(departure, arrival);
    }

    private Schedule(final Departure departure, final Arrival arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }
}
