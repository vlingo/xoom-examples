// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

import java.util.Date;

public class Schedule {

    public final Date scheduledDeparture;
    public final Date scheduledArrival;
    public final DepartureStatus departureStatus;

    public static Schedule on(final Date scheduledDeparture,
                              final Date scheduledArrival,
                              final DepartureStatus departureStatus) {
        return new Schedule(scheduledDeparture, scheduledArrival, departureStatus);
    }

    private Schedule(final Date scheduledDeparture,
                     final Date scheduledArrival,
                     final DepartureStatus departureStatus) {
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.departureStatus = departureStatus;
    }

    public Schedule arrivedOn(final Date scheduledArrival) {
        return new Schedule(this.scheduledDeparture, scheduledArrival, this.departureStatus);
    }

    public Schedule departedOn(final Date actuallyDepartedOn) {
        return new Schedule(this.scheduledDeparture, this.scheduledArrival, DepartureStatus.from(this.scheduledDeparture, actuallyDepartedOn));
    }
}
