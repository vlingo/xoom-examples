// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DepartureStatusData {

    public final long actual;
    public final long delayedBy;

    public DepartureStatusData(final LocalDateTime actual, final long delayedBy) {
        this.actual = actual.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.delayedBy = delayedBy;
    }
}
