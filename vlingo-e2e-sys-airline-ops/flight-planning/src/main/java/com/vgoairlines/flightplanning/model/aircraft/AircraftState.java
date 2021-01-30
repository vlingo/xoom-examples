// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.aircraft;

import io.vlingo.symbio.store.object.StateObject;

public class AircraftState extends StateObject {

    public final String id;
    public final String model;
    public final String serialNumber;
    public final String tailNumber;

    public static AircraftState identifiedById(final String id) {
        return new AircraftState(id, null, null, null);
    }

    private AircraftState(final String id,
                          final String model,
                          final String serialNumber,
                          final String tailNumber) {
        this.id = id;
        this.model = model;
        this.serialNumber = serialNumber;
        this.tailNumber = tailNumber;
    }

    public AircraftState pool(final String model, final String serialNumber, final String tailNumber) {
        return new AircraftState(id, model, serialNumber, tailNumber);
    }
}
