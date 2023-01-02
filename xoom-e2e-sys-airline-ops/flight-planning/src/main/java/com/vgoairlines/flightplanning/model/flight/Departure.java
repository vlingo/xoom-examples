// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.flight;

import java.util.Date;

public class Departure {

    public final Airport airport;
    public final Date plannedFor;

    public static Departure on(final Airport airport, final Date planedFor) {
        return new Departure(airport, planedFor);
    }

    private Departure(final Airport airport, final Date plannedFor) {
        this.airport = airport;
        this.plannedFor = plannedFor;
    }
}
