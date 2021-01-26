// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

import java.time.LocalDateTime;

public class Schedule {

    public final LocalDateTime scheduledDeparture;
    public final LocalDateTime scheduledArrival;
    public final DepartureStatus departureStatus;

    public static Schedule on(final LocalDateTime scheduledDeparture, final LocalDateTime scheduledArrival) {
        return new Schedule(scheduledDeparture, scheduledArrival, null);
    }

    private Schedule(final LocalDateTime scheduledDeparture,
                     final LocalDateTime scheduledArrival,
                     final DepartureStatus departureStatus) {
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.departureStatus = departureStatus;
    }

    public Schedule departedOn(final LocalDateTime actuallyDepartedOn) {
        return new Schedule(this.scheduledDeparture, this.scheduledArrival, DepartureStatus.from(this.scheduledDeparture, actuallyDepartedOn));
    }
}
