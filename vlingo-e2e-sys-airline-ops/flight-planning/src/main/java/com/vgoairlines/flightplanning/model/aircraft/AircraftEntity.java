// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package com.vgoairlines.flightplanning.model.aircraft;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public class AircraftEntity extends StatefulEntity<AircraftState> implements Aircraft {

    private AircraftState state;

    public AircraftEntity(final String id) {
        super(id);
        this.state = AircraftState.identifiedById(id);
    }

    @Override
    public Completes<AircraftState> pool(final String aircraftId, final Denomination denomination) {
        final AircraftState stateArg = state.pool(aircraftId, denomination);
        return apply(stateArg, new AircraftPooled(stateArg), () -> state);
    }

    @Override
    protected void state(final AircraftState state) {
        this.state = state;
    }

    @Override
    protected Class<AircraftState> stateType() {
        return AircraftState.class;
    }
}
