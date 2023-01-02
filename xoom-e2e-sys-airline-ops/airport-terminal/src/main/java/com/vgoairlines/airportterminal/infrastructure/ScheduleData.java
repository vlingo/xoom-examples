// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure;

import java.util.Date;

public class ScheduleData {

    public final Date scheduledDeparture;
    public final Date scheduledArrival;
    public final DepartureStatusData departureStatus;

    public ScheduleData(final Date scheduledDeparture,
                        final Date scheduledArrival,
                        final DepartureStatusData departureStatus) {
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.departureStatus = departureStatus;
    }

}
