// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure;

import java.time.LocalDateTime;

public class DepartureData {

    public final AirportData airport;
    public final LocalDateTime plannedFor;

    public DepartureData(final AirportData airport, final LocalDateTime plannedFor) {
        this.airport = airport;
        this.plannedFor = plannedFor;
    }
}
