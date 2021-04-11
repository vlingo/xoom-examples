// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.infrastructure;

public class EquipmentData {

    public final String carrier;
    public final String tailNumber;

    public EquipmentData(final String carrier, final String tailNumber) {
        this.carrier = carrier;
        this.tailNumber = tailNumber;
    }
}
