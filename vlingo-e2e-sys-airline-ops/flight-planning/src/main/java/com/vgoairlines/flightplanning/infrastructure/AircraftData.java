// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure;

import com.vgoairlines.flightplanning.model.aircraft.AircraftState;

public class AircraftData {

    public final String id;
    public final String model;
    public final String serialNumber;
    public final String tailNumber;

    public AircraftData(final AircraftState state) {
        this.id = state.id;
        this.model = state.model;
        this.serialNumber = state.serialNumber;
        this.tailNumber = state.tailNumber;
    }

    public static AircraftData from(final AircraftState state) {
        return new AircraftData(state);
    }
}