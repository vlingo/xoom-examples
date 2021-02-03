// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.model.flight;

public class Airport {

    public final String code;

    public static Airport with(final String code) {
        return new Airport(code);
    }

    private Airport(final String code) {
        this.code = code;
    }
}
