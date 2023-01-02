// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure;

import java.util.Date;

public class ArrivalData {

    public final AirportData airport;
    public final Date plannedFor;

    public ArrivalData(final AirportData airport, final Date plannedFor) {
        this.airport = airport;
        this.plannedFor = plannedFor;
    }

}
