// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DepartureStatus {

    public final LocalDateTime actual;
    public final long delayedBy;

    public static DepartureStatus from(final LocalDateTime scheduledDeparture,
                                       final LocalDateTime actual) {
        return new DepartureStatus(actual, scheduledDeparture);
    }

    private DepartureStatus(final LocalDateTime scheduledDeparture,
                            final LocalDateTime actual) {
        this.actual = actual;
        this.delayedBy = actual.isAfter(scheduledDeparture) ?
                       scheduledDeparture.until(actual, ChronoUnit.SECONDS) : 0;
    }
}
