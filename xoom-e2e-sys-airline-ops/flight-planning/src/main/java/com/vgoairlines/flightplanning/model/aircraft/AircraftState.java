// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.aircraft;

import io.vlingo.xoom.symbio.store.object.StateObject;

public class AircraftState extends StateObject {

    public final String id;
    public final String aircraftId;
    public final Denomination denomination;

    public static AircraftState identifiedById(final String id) {
        return new AircraftState(id, null, null);
    }

    private AircraftState(final String id,
                          final String aircraftId,
                          final Denomination denomination) {
        this.id = id;
        this.aircraftId = aircraftId;
        this.denomination = denomination;
    }

    public AircraftState pool(final String aircraftId,
                              final Denomination denomination) {
        return new AircraftState(id, aircraftId, denomination);
    }
}
