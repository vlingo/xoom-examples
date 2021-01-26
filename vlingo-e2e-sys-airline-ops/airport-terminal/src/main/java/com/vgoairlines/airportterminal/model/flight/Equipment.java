// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.airportterminal.model.flight;

public class Equipment {

    public final String carrier;
    public final String tailNumber;

    public static Equipment of(final String carrier, final String tailNumber) {
        return new Equipment(carrier, tailNumber);
    }

    private Equipment(final String carrier, final String tailNumber) {
        this.carrier = carrier;
        this.tailNumber = tailNumber;
    }
}
