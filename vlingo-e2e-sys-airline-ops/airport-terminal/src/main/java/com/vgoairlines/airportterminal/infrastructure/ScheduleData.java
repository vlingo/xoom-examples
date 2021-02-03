// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure;

import java.time.LocalDateTime;

public class ScheduleData {

    public final LocalDateTime scheduledDeparture;
    public final LocalDateTime scheduledArrival;
    public final DepartureStatusData departureStatus;

    public ScheduleData(final LocalDateTime scheduledDeparture,
                         final LocalDateTime scheduledArrival,
                         final DepartureStatusData departureStatus) {
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.departureStatus = departureStatus;
    }

}
