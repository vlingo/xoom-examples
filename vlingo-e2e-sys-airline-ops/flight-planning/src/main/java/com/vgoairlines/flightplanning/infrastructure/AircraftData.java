// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure;

public class AircraftData {

    public final String id;
    public final String model;
    public final String serialNumber;
    public final String tailNumber;

    public AircraftData(final String id,
                    final String model,
                    final String serialNumber,
                    final String tailNumber) {
        this.id = id;
        this.model = model;
        this.serialNumber = serialNumber;
        this.tailNumber = tailNumber;
    }
}
