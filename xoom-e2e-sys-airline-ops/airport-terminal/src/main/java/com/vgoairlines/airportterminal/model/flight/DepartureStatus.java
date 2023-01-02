// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

import java.util.Date;

public class DepartureStatus {

    public final Date actual;
    public final long delayedBy;

    public static DepartureStatus from(final Date scheduledDeparture,
                                       final Date actual) {
        return new DepartureStatus(actual, scheduledDeparture);
    }

    public static DepartureStatus unknown() {
        return new DepartureStatus();
    }

    private DepartureStatus(final Date scheduledDeparture,
                            final Date actual) {
        this.actual = actual;
        this.delayedBy = actual.compareTo(scheduledDeparture) > 0 ?
                actual.getTime()-scheduledDeparture.getTime()/1000 : 0;
    }

    private DepartureStatus() {
        this.actual = null;
        this.delayedBy = 0;
    }
}
