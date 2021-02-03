// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.flight;

public class Aircraft {

    public final String id;
    public final String model;
    public final String serialNumber;
    public final String tailNumber;

    public static Aircraft of(final String id,
                              final String model,
                              final String serialNumber,
                              final String tailNumber) {
        return new Aircraft(id, model,serialNumber, tailNumber);
    }
    
    private Aircraft(final String id,
                    final String model,
                    final String serialNumber,
                    final String tailNumber) {
        this.id = id;
        this.model = model;
        this.serialNumber = serialNumber;
        this.tailNumber = tailNumber;
    }
}
